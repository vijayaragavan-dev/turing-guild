import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { submissionsApi } from '../../api/submissions';
import { eventsApi } from '../../api/events';
import type { Event, Submission } from '../../types';
import { ArrowLeft, CheckCircle } from 'lucide-react';

export function SubmissionPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [submission, setSubmission] = useState<Submission | null>(null);
  const [event, setEvent] = useState<Event | null>(null);
  const [answers, setAnswers] = useState<Record<number, string>>({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (id) {
          const sub = await submissionsApi.getSubmission(parseInt(id));
          setSubmission(sub);
          
          if (sub.eventId) {
            const evt = await eventsApi.getEventById(sub.eventId);
            setEvent(evt);
            
            const initialAnswers: Record<number, string> = {};
            evt.questions?.forEach(q => {
              initialAnswers[q.id] = '';
            });
            setAnswers(initialAnswers);
          }
        }
      } catch (error) {
        console.error('Failed to fetch data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleAnswerChange = (questionId: number, value: string) => {
    setAnswers(prev => ({ ...prev, [questionId]: value }));
  };

  const handleSubmit = async () => {
    if (!submission) return;
    
    setSubmitting(true);
    setError('');
    
    try {
      const answerList = Object.entries(answers).map(([questionId, answerText]) => ({
        questionId: parseInt(questionId),
        answerText,
      }));
      
      await submissionsApi.submitAnswers(submission.id, answerList);
      navigate('/student/results');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to submit answers');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!submission || !event) {
    return (
      <div className="text-center py-12">
        <h2 className="text-lg font-medium text-gray-900">Submission not found</h2>
      </div>
    );
  }

  if (submission.status !== 'IN_PROGRESS') {
    return (
      <div className="max-w-3xl mx-auto space-y-6">
        <div className="bg-white rounded-lg shadow p-6 text-center">
          <CheckCircle className="mx-auto h-12 w-12 text-green-500" />
          <h2 className="mt-4 text-lg font-medium text-gray-900">Already Submitted</h2>
          <p className="mt-2 text-gray-500">
            Score: {submission.totalScore} / {event.totalMarks}
          </p>
          <button
            onClick={() => navigate('/student/results')}
            className="mt-4 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90"
          >
            View Results
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <button
        onClick={() => navigate(-1)}
        className="flex items-center text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft size={16} className="mr-2" />
        Back
      </button>

      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-2xl font-bold text-gray-900">{event.title}</h1>
        <p className="text-gray-500 mt-2">{event.durationMinutes} minutes | {event.totalMarks} marks</p>
      </div>

      <div className="space-y-6">
        {event.questions?.map((question, idx) => (
          <div key={question.id} className="bg-white rounded-lg shadow p-6">
            <div className="flex items-start justify-between">
              <h3 className="font-medium text-gray-900">
                {idx + 1}. {question.questionText}
              </h3>
              <span className="text-sm text-gray-500">{question.marks} marks</span>
            </div>

            {question.questionType === 'MCQ' && question.options && (
              <div className="mt-4 space-y-2">
                {question.options.map((option) => (
                  <label key={option.id} className="flex items-center space-x-3 p-3 border rounded hover:bg-gray-50">
                    <input
                      type="radio"
                      name={`question-${question.id}`}
                      value={option.optionText}
                      checked={answers[question.id] === option.optionText}
                      onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                      className="text-primary focus:ring-primary"
                    />
                    <span>{option.optionText}</span>
                  </label>
                ))}
              </div>
            )}

            {(question.questionType === 'CODING' || question.questionType === 'FILL_MISSING_CODE') && (
              <div className="mt-4">
                {question.codingTemplate && (
                  <pre className="p-4 bg-gray-100 rounded text-sm mb-4 overflow-x-auto">
                    {question.codingTemplate}
                  </pre>
                )}
                <textarea
                  value={answers[question.id] || ''}
                  onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                  className="w-full p-3 border rounded font-mono text-sm"
                  rows={10}
                  placeholder="Write your code here..."
                />
              </div>
            )}

            {(question.questionType === 'OUTPUT_PREDICTION' || question.questionType === 'DEBUGGING') && (
              <div className="mt-4">
                {question.sampleInput && (
                  <div className="mb-4">
                    <p className="text-sm font-medium text-gray-700 mb-1">Input:</p>
                    <pre className="p-3 bg-gray-100 rounded text-sm">{question.sampleInput}</pre>
                  </div>
                )}
                <input
                  type="text"
                  value={answers[question.id] || ''}
                  onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                  className="w-full p-3 border rounded"
                  placeholder="Enter your answer"
                />
              </div>
            )}

            {(question.questionType === 'FILL_BLANKS' || question.questionType === 'NUMERICAL' || question.questionType === 'LOGICAL') && (
              <div className="mt-4">
                <input
                  type="text"
                  value={answers[question.id] || ''}
                  onChange={(e) => handleAnswerChange(question.id, e.target.value)}
                  className="w-full p-3 border rounded"
                  placeholder="Enter your answer"
                />
              </div>
            )}
          </div>
        ))}
      </div>

      {error && (
        <div className="bg-red-50 text-red-600 p-3 rounded text-sm">{error}</div>
      )}

      <div className="bg-white rounded-lg shadow p-6">
        <button
          onClick={handleSubmit}
          disabled={submitting}
          className="w-full py-3 px-4 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:opacity-50"
        >
          {submitting ? 'Submitting...' : 'Submit Answers'}
        </button>
      </div>
    </div>
  );
}
