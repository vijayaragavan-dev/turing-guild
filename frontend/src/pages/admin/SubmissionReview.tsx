import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { submissionsApi } from '../../api/submissions';
import type { Submission } from '../../types';
import { ArrowLeft, Save } from 'lucide-react';

export function SubmissionReview() {
  const { eventId } = useParams<{ eventId: string }>();
  const navigate = useNavigate();
  const [submissions, setSubmissions] = useState<Submission[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedSubmission, setSelectedSubmission] = useState<Submission | null>(null);
  const [evaluation, setEvaluation] = useState<Record<number, { scoreAwarded: number; feedback: string }>>({});

  useEffect(() => {
    const fetchSubmissions = async () => {
      try {
        if (eventId) {
          const data = await submissionsApi.getSubmissionsByEvent(parseInt(eventId));
          setSubmissions(data);
        }
      } catch (error) {
        console.error('Failed to fetch submissions:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchSubmissions();
  }, [eventId]);

  const handleSelectSubmission = async (submission: Submission) => {
    try {
      const detailed = await submissionsApi.getSubmissionById(submission.id);
      setSelectedSubmission(detailed);
      
      const initialEval: Record<number, { scoreAwarded: number; feedback: string }> = {};
      detailed.answers?.forEach(ans => {
        initialEval[ans.id] = {
          scoreAwarded: ans.scoreAwarded,
          feedback: ans.feedback || '',
        };
      });
      setEvaluation(initialEval);
    } catch (error) {
      console.error('Failed to fetch submission details:', error);
    }
  };

  const handleEvaluate = async () => {
    if (!selectedSubmission) return;

    try {
      const answers = Object.entries(evaluation).map(([answerId, data]) => ({
        answerId: parseInt(answerId),
        scoreAwarded: data.scoreAwarded,
        feedback: data.feedback,
      }));

      await submissionsApi.evaluateSubmission(selectedSubmission.id, answers);
      alert('Evaluation saved successfully');
      setSelectedSubmission(null);
      
      if (eventId) {
        const data = await submissionsApi.getSubmissionsByEvent(parseInt(eventId));
        setSubmissions(data);
      }
    } catch (error) {
      console.error('Failed to evaluate:', error);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <button
        onClick={() => navigate('/admin/events')}
        className="flex items-center text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft size={16} className="mr-2" />
        Back to events
      </button>

      <h1 className="text-2xl font-bold text-gray-900">Review Submissions</h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow">
          <div className="p-4 border-b">
            <h2 className="font-semibold">Submissions ({submissions.length})</h2>
          </div>
          <div className="p-4 max-h-96 overflow-y-auto">
            {submissions.map((sub) => (
              <button
                key={sub.id}
                onClick={() => handleSelectSubmission(sub)}
                className={`w-full text-left p-3 rounded mb-2 ${
                  selectedSubmission?.id === sub.id ? 'bg-primary text-white' : 'hover:bg-gray-50'
                }`}
              >
                <p className="font-medium">{sub.userBatchNumber}</p>
                <p className="text-sm opacity-75">{sub.status} | Score: {sub.totalScore}</p>
              </button>
            ))}
          </div>
        </div>

        <div className="lg:col-span-2 bg-white rounded-lg shadow">
          {selectedSubmission ? (
            <div className="p-6">
              <div className="flex justify-between items-center mb-6">
                <div>
                  <h2 className="text-lg font-semibold">
                    {selectedSubmission.userBatchNumber}'s Submission
                  </h2>
                  <p className="text-sm text-gray-500">
                    Status: {selectedSubmission.status} | Score: {selectedSubmission.totalScore}
                  </p>
                </div>
                <button
                  onClick={handleEvaluate}
                  className="flex items-center space-x-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                >
                  <Save size={16} />
                  <span>Save Evaluation</span>
                </button>
              </div>

              <div className="space-y-6">
                {selectedSubmission.answers?.map((answer) => (
                  <div key={answer.id} className="border rounded-lg p-4">
                    <p className="font-medium mb-2">Question {answer.questionId}</p>
                    <p className="text-sm text-gray-600 mb-4">
                      Answer: {answer.answerText || '(No answer)'}
                    </p>
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Score</label>
                        <input
                          type="number"
                          min="0"
                          value={evaluation[answer.id]?.scoreAwarded || 0}
                          onChange={(e) => setEvaluation({
                            ...evaluation,
                            [answer.id]: {
                              ...evaluation[answer.id],
                              scoreAwarded: parseInt(e.target.value),
                            },
                          })}
                          className="mt-1 block w-full px-3 py-2 border rounded-md"
                        />
                      </div>
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Feedback</label>
                        <input
                          type="text"
                          value={evaluation[answer.id]?.feedback || ''}
                          onChange={(e) => setEvaluation({
                            ...evaluation,
                            [answer.id]: {
                              ...evaluation[answer.id],
                              feedback: e.target.value,
                            },
                          })}
                          className="mt-1 block w-full px-3 py-2 border rounded-md"
                        />
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <div className="flex items-center justify-center h-64 text-gray-500">
              Select a submission to review
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
