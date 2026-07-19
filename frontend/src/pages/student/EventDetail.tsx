import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { eventsApi } from '../../api/events';
import { submissionsApi } from '../../api/submissions';
import type { Event } from '../../types';
import { Clock, Award, ArrowLeft } from 'lucide-react';

export function EventDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [event, setEvent] = useState<Event | null>(null);
  const [loading, setLoading] = useState(true);
  const [joining, setJoining] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchEvent = async () => {
      try {
        if (id) {
          const data = await eventsApi.getEventById(parseInt(id));
          setEvent(data);
        }
      } catch (error) {
        console.error('Failed to fetch event:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchEvent();
  }, [id]);

  const handleJoin = async () => {
    if (!event) return;
    
    setJoining(true);
    setError('');
    
    try {
      const submission = await submissionsApi.joinEvent(event.id);
      navigate(`/student/submissions/${submission.id}`);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to join event');
    } finally {
      setJoining(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  if (!event) {
    return (
      <div className="text-center py-12">
        <h2 className="text-lg font-medium text-gray-900">Event not found</h2>
        <button
          onClick={() => navigate('/student/events')}
          className="mt-4 text-primary hover:underline"
        >
          Back to events
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      <button
        onClick={() => navigate('/student/events')}
        className="flex items-center text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft size={16} className="mr-2" />
        Back to events
      </button>

      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-start justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">{event.title}</h1>
            <span className="inline-block mt-2 px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded">
              {event.category}
            </span>
          </div>
        </div>

        {event.description && (
          <p className="mt-4 text-gray-600">{event.description}</p>
        )}

        <div className="mt-6 flex items-center space-x-6 text-sm text-gray-500">
          <div className="flex items-center space-x-2">
            <Clock size={16} />
            <span>{event.durationMinutes} minutes</span>
          </div>
          <div className="flex items-center space-x-2">
            <Award size={16} />
            <span>{event.totalMarks} total marks</span>
          </div>
        </div>

        {event.questions && event.questions.length > 0 && (
          <div className="mt-6">
            <h3 className="font-medium text-gray-900">Questions ({event.questions.length})</h3>
            <ul className="mt-2 space-y-2">
              {event.questions.map((q, idx) => (
                <li key={q.id} className="text-sm text-gray-600">
                  {idx + 1}. {q.questionText.substring(0, 100)}...
                  <span className="ml-2 text-gray-400">({q.marks} marks)</span>
                </li>
              ))}
            </ul>
          </div>
        )}

        {error && (
          <div className="mt-4 bg-red-50 text-red-600 p-3 rounded text-sm">{error}</div>
        )}

        <div className="mt-6">
          <button
            onClick={handleJoin}
            disabled={joining}
            className="w-full py-3 px-4 bg-primary text-white rounded-lg hover:bg-primary/90 disabled:opacity-50"
          >
            {joining ? 'Joining...' : 'Join Event'}
          </button>
        </div>
      </div>
    </div>
  );
}
