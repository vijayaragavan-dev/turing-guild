import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { eventsApi } from '../../api/events';
import type { Event } from '../../types';
import { Plus, Edit, Trash2, Play, Square } from 'lucide-react';

export function EventManagement() {
  const [events, setEvents] = useState<Event[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const data = await eventsApi.getAllEvents();
      setEvents(data);
    } catch (error) {
      console.error('Failed to fetch events:', error);
    } finally {
      setLoading(false);
    }
  };

  const handlePublish = async (id: number) => {
    try {
      await eventsApi.publishEvent(id);
      fetchEvents();
    } catch (error: any) {
      alert(error.response?.data?.message || 'Failed to publish event');
    }
  };

  const handleClose = async (id: number) => {
    try {
      await eventsApi.closeEvent(id);
      fetchEvents();
    } catch (error) {
      console.error('Failed to close event:', error);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to delete this event?')) {
      try {
        await eventsApi.deleteEvent(id);
        fetchEvents();
      } catch (error) {
        console.error('Failed to delete event:', error);
      }
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
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Event Management</h1>
        <Link
          to="/admin/events/new"
          className="flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90"
        >
          <Plus size={16} />
          <span>Create Event</span>
        </Link>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Title
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Category
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Marks
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {events.map((event) => (
              <tr key={event.id}>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div className="text-sm font-medium text-gray-900">{event.title}</div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {event.category}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 text-xs rounded ${
                    event.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' :
                    event.status === 'DRAFT' ? 'bg-yellow-100 text-yellow-800' :
                    event.status === 'CLOSED' ? 'bg-gray-100 text-gray-800' :
                    'bg-red-100 text-red-800'
                  }`}>
                    {event.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {event.totalMarks}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm space-x-2">
                  <Link to={`/admin/events/${event.id}`} className="text-blue-600 hover:text-blue-900">
                    <Edit size={16} />
                  </Link>
                  {event.status === 'DRAFT' && (
                    <button onClick={() => handlePublish(event.id)} className="text-green-600 hover:text-green-900">
                      <Play size={16} />
                    </button>
                  )}
                  {event.status === 'PUBLISHED' && (
                    <button onClick={() => handleClose(event.id)} className="text-yellow-600 hover:text-yellow-900">
                      <Square size={16} />
                    </button>
                  )}
                  <button onClick={() => handleDelete(event.id)} className="text-red-600 hover:text-red-900">
                    <Trash2 size={16} />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
