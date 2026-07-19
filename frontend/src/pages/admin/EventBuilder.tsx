import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventsApi } from '../../api/events';
import type { Event, Question } from '../../types';
import { Plus, Trash2, ArrowLeft } from 'lucide-react';

export function EventBuilder() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [event, setEvent] = useState<Event | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(true);
  const [showQuestionForm, setShowQuestionForm] = useState(false);
  const [questionForm, setQuestionForm] = useState({
    questionType: 'MCQ' as Question['questionType'],
    questionText: '',
    marks: 1,
    orderIndex: 0,
    codingTemplate: '',
    expectedOutput: '',
    sampleInput: '',
    sampleOutput: '',
    options: [] as { optionText: string; isCorrect: boolean }[],
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (id) {
          const [eventData, questionsData] = await Promise.all([
            eventsApi.getEventById(parseInt(id)),
            eventsApi.getEventQuestions(parseInt(id)),
          ]);
          setEvent(eventData);
          setQuestions(questionsData);
        }
      } catch (error) {
        console.error('Failed to fetch data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [id]);

  const handleAddQuestion = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;

    try {
      await eventsApi.addQuestion(parseInt(id), questionForm);
      const questionsData = await eventsApi.getEventQuestions(parseInt(id));
      setQuestions(questionsData);
      setShowQuestionForm(false);
      setQuestionForm({
        questionType: 'MCQ',
        questionText: '',
        marks: 1,
        orderIndex: questions.length,
        codingTemplate: '',
        expectedOutput: '',
        sampleInput: '',
        sampleOutput: '',
        options: [],
      });
    } catch (error) {
      console.error('Failed to add question:', error);
    }
  };

  const handleDeleteQuestion = async (questionId: number) => {
    if (confirm('Delete this question?')) {
      try {
        await eventsApi.deleteQuestion(questionId);
        setQuestions(questions.filter(q => q.id !== questionId));
      } catch (error) {
        console.error('Failed to delete question:', error);
      }
    }
  };

  const addOption = () => {
    setQuestionForm({
      ...questionForm,
      options: [...questionForm.options, { optionText: '', isCorrect: false }],
    });
  };

  const updateOption = (index: number, field: string, value: any) => {
    const newOptions = [...questionForm.options];
    (newOptions[index] as any)[field] = value;
    setQuestionForm({ ...questionForm, options: newOptions });
  };

  const removeOption = (index: number) => {
    setQuestionForm({
      ...questionForm,
      options: questionForm.options.filter((_, i) => i !== index),
    });
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto space-y-6">
      <button
        onClick={() => navigate('/admin/events')}
        className="flex items-center text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft size={16} className="mr-2" />
        Back to events
      </button>

      <div className="bg-white rounded-lg shadow p-6">
        <h1 className="text-2xl font-bold text-gray-900">{event?.title}</h1>
        <p className="text-gray-500 mt-2">{event?.category} | {event?.status}</p>
      </div>

      <div className="bg-white rounded-lg shadow">
        <div className="p-6 border-b flex justify-between items-center">
          <h2 className="text-lg font-semibold">Questions ({questions.length})</h2>
          {event?.status === 'DRAFT' && (
            <button
              onClick={() => setShowQuestionForm(true)}
              className="flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90"
            >
              <Plus size={16} />
              <span>Add Question</span>
            </button>
          )}
        </div>
        <div className="p-6">
          {questions.length === 0 ? (
            <p className="text-center text-gray-500 py-8">No questions yet</p>
          ) : (
            <div className="space-y-4">
              {questions.map((q, idx) => (
                <div key={q.id} className="border rounded-lg p-4">
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="font-medium">{idx + 1}. {q.questionText}</p>
                      <p className="text-sm text-gray-500 mt-1">
                        {q.questionType} | {q.marks} marks
                      </p>
                    </div>
                    {event?.status === 'DRAFT' && (
                      <button
                        onClick={() => handleDeleteQuestion(q.id)}
                        className="text-red-600 hover:text-red-900"
                      >
                        <Trash2 size={16} />
                      </button>
                    )}
                  </div>
                  {q.options && q.options.length > 0 && (
                    <div className="mt-3 space-y-1">
                      {q.options.map((opt) => (
                        <p key={opt.id} className="text-sm text-gray-600">- {opt.optionText}</p>
                      ))}
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {showQuestionForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 overflow-y-auto">
          <div className="bg-white rounded-lg p-6 w-full max-w-2xl my-8">
            <h2 className="text-lg font-semibold mb-4">Add Question</h2>
            <form onSubmit={handleAddQuestion} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">Question Type</label>
                <select
                  value={questionForm.questionType}
                  onChange={(e) => setQuestionForm({ ...questionForm, questionType: e.target.value as Question['questionType'] })}
                  className="mt-1 block w-full px-3 py-2 border rounded-md"
                >
                  <option value="MCQ">MCQ</option>
                  <option value="CODING">Coding</option>
                  <option value="DEBUGGING">Debugging</option>
                  <option value="OUTPUT_PREDICTION">Output Prediction</option>
                  <option value="FILL_MISSING_CODE">Fill Missing Code</option>
                  <option value="LOGICAL">Logical</option>
                  <option value="NUMERICAL">Numerical</option>
                  <option value="FILL_BLANKS">Fill Blanks</option>
                  <option value="MATCH">Match</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Question Text</label>
                <textarea
                  required
                  value={questionForm.questionText}
                  onChange={(e) => setQuestionForm({ ...questionForm, questionText: e.target.value })}
                  className="mt-1 block w-full px-3 py-2 border rounded-md"
                  rows={3}
                />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700">Marks</label>
                  <input
                    type="number"
                    min="1"
                    value={questionForm.marks}
                    onChange={(e) => setQuestionForm({ ...questionForm, marks: parseInt(e.target.value) })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700">Order Index</label>
                  <input
                    type="number"
                    min="0"
                    value={questionForm.orderIndex}
                    onChange={(e) => setQuestionForm({ ...questionForm, orderIndex: parseInt(e.target.value) })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md"
                  />
                </div>
              </div>

              {(questionForm.questionType === 'MCQ' || questionForm.questionType === 'LOGICAL') && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Options</label>
                  {questionForm.options.map((opt, idx) => (
                    <div key={idx} className="flex items-center space-x-2 mb-2">
                      <input
                        type="text"
                        placeholder="Option text"
                        value={opt.optionText}
                        onChange={(e) => updateOption(idx, 'optionText', e.target.value)}
                        className="flex-1 px-3 py-2 border rounded-md"
                      />
                      <label className="flex items-center space-x-1">
                        <input
                          type="checkbox"
                          checked={opt.isCorrect}
                          onChange={(e) => updateOption(idx, 'isCorrect', e.target.checked)}
                        />
                        <span className="text-sm">Correct</span>
                      </label>
                      <button
                        type="button"
                        onClick={() => removeOption(idx)}
                        className="text-red-600"
                      >
                        <Trash2 size={16} />
                      </button>
                    </div>
                  ))}
                  <button
                    type="button"
                    onClick={addOption}
                    className="text-sm text-primary hover:underline"
                  >
                    + Add Option
                  </button>
                </div>
              )}

              {(questionForm.questionType === 'OUTPUT_PREDICTION' || questionForm.questionType === 'DEBUGGING' || questionForm.questionType === 'NUMERICAL' || questionForm.questionType === 'FILL_BLANKS') && (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Expected Output</label>
                  <input
                    type="text"
                    value={questionForm.expectedOutput}
                    onChange={(e) => setQuestionForm({ ...questionForm, expectedOutput: e.target.value })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md"
                  />
                </div>
              )}

              {(questionForm.questionType === 'CODING' || questionForm.questionType === 'FILL_MISSING_CODE') && (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Coding Template</label>
                  <textarea
                    value={questionForm.codingTemplate}
                    onChange={(e) => setQuestionForm({ ...questionForm, codingTemplate: e.target.value })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md font-mono"
                    rows={5}
                  />
                </div>
              )}

              <div className="flex justify-end space-x-3 mt-6">
                <button
                  type="button"
                  onClick={() => setShowQuestionForm(false)}
                  className="px-4 py-2 border rounded-md hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-primary text-white rounded-md hover:bg-primary/90"
                >
                  Add Question
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
