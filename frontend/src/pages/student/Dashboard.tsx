import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { eventsApi } from '../../api/events';
import { submissionsApi } from '../../api/submissions';
import type { Event, Submission } from '../../types';
import { Calendar, Trophy, ClipboardList } from 'lucide-react';

export function StudentDashboard() {
  const [events, setEvents] = useState<Event[]>([]);
  const [results, setResults] = useState<Submission[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [eventsData, resultsData] = await Promise.all([
          eventsApi.getPublishedEvents(),
          submissionsApi.getMyResults(),
        ]);
        setEvents(eventsData);
        setResults(resultsData);
      } catch (error) {
        console.error('Failed to fetch data:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold text-gray-900">Student Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Calendar className="text-blue-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Available Events</p>
              <p className="text-2xl font-bold">{events.length}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <ClipboardList className="text-green-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Completed</p>
              <p className="text-2xl font-bold">
                {results.filter(r => r.status === 'SUBMITTED' || r.status === 'EVALUATED').length}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Trophy className="text-yellow-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Total Score</p>
              <p className="text-2xl font-bold">
                {results.reduce((sum, r) => sum + r.totalScore, 0)}
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow">
        <div className="p-6 border-b">
          <h2 className="text-lg font-semibold">Recent Events</h2>
        </div>
        <div className="p-6">
          {events.length === 0 ? (
            <p className="text-gray-500 text-center py-8">No events available</p>
          ) : (
            <div className="space-y-4">
              {events.slice(0, 5).map((event) => (
                <Link
                  key={event.id}
                  to={`/student/events/${event.id}`}
                  className="block p-4 border rounded-lg hover:bg-gray-50 transition-colors"
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-medium">{event.title}</h3>
                      <p className="text-sm text-gray-500">{event.category}</p>
                    </div>
                    <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded">
                      {event.totalMarks} marks
                    </span>
                  </div>
                </Link>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
