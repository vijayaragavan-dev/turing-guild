import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { eventsApi } from '../../api/events';
import type { Event } from '../../types';
import { Calendar, Clock, Award } from 'lucide-react';

export function EventList() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<string>('ALL');

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const data = await eventsApi.getPublishedEvents();
        setEvents(data);
      } catch (error) {
        console.error('Failed to fetch events:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  const filteredEvents = filter === 'ALL' 
    ? events 
    : events.filter(e => e.category === filter);

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Events</h1>
        <select
          value={filter}
          onChange={(e) => setFilter(e.target.value)}
          className="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
        >
          <option value="ALL">All Categories</option>
          <option value="PROGRAMMING">Programming</option>
          <option value="APTITUDE">Aptitude</option>
          <option value="VERBAL">Verbal</option>
          <option value="CORE_CS">Core CS</option>
        </select>
      </div>

      {filteredEvents.length === 0 ? (
        <div className="text-center py-12 bg-white rounded-lg shadow">
          <Calendar className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No events</h3>
          <p className="mt-1 text-sm text-gray-500">No events available at the moment.</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {filteredEvents.map((event) => (
            <Link
              key={event.id}
              to={`/student/events/${event.id}`}
              className="block bg-white rounded-lg shadow hover:shadow-md transition-shadow"
            >
              <div className="p-6">
                <div className="flex items-start justify-between">
                  <h3 className="text-lg font-semibold text-gray-900">{event.title}</h3>
                  <span className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded">
                    {event.category}
                  </span>
                </div>
                
                {event.description && (
                  <p className="mt-2 text-sm text-gray-500 line-clamp-2">{event.description}</p>
                )}
                
                <div className="mt-4 flex items-center space-x-4 text-sm text-gray-500">
                  <div className="flex items-center space-x-1">
                    <Clock size={14} />
                    <span>{event.durationMinutes} min</span>
                  </div>
                  <div className="flex items-center space-x-1">
                    <Award size={14} />
                    <span>{event.totalMarks} marks</span>
                  </div>
                </div>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
