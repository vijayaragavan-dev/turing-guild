import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { studentsApi } from '../../api/students';
import { eventsApi } from '../../api/events';
import { adminsApi } from '../../api/admins';
import type { Student, Event } from '../../types';
import { Users, Calendar, Trophy, Plus, Shield } from 'lucide-react';

export function AdminDashboard() {
  const [students, setStudents] = useState<Student[]>([]);
  const [events, setEvents] = useState<Event[]>([]);
  const [adminCount, setAdminCount] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [studentsData, eventsData, adminsData] = await Promise.all([
          studentsApi.getAllStudents(),
          eventsApi.getAllEvents(),
          adminsApi.getAllAdmins(),
        ]);
        setStudents(studentsData);
        setEvents(eventsData);
        setAdminCount(adminsData.length);
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
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-900">Admin Dashboard</h1>
        <Link
          to="/admin/events/new"
          className="flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90"
        >
          <Plus size={16} />
          <span>New Event</span>
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Shield className="text-slate-600" size={24} />
            <div>
              <p className="text-sm text-gray-500">Total Admins</p>
              <p className="text-2xl font-bold">{adminCount}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Users className="text-blue-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Total Students</p>
              <p className="text-2xl font-bold">{students.length}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Calendar className="text-green-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Total Events</p>
              <p className="text-2xl font-bold">{events.length}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center space-x-3">
            <Trophy className="text-yellow-500" size={24} />
            <div>
              <p className="text-sm text-gray-500">Published Events</p>
              <p className="text-2xl font-bold">
                {events.filter(e => e.status === 'PUBLISHED').length}
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow">
          <div className="p-6 border-b flex justify-between items-center">
            <h2 className="text-lg font-semibold">Recent Students</h2>
            <Link to="/admin/students" className="text-sm text-primary hover:underline">
              View all
            </Link>
          </div>
          <div className="p-6">
            {students.slice(0, 5).map((student) => (
              <div key={student.id} className="flex items-center justify-between py-2">
                <div>
                  <p className="font-medium">{student.fullName}</p>
                  <p className="text-sm text-gray-500">{student.batchNumber}</p>
                </div>
                <span className={`px-2 py-1 text-xs rounded ${student.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                  {student.isActive ? 'Active' : 'Inactive'}
                </span>
              </div>
            ))}
          </div>
        </div>

        <div className="bg-white rounded-lg shadow">
          <div className="p-6 border-b flex justify-between items-center">
            <h2 className="text-lg font-semibold">Recent Events</h2>
            <Link to="/admin/events" className="text-sm text-primary hover:underline">
              View all
            </Link>
          </div>
          <div className="p-6">
            {events.slice(0, 5).map((event) => (
              <div key={event.id} className="flex items-center justify-between py-2">
                <div>
                  <p className="font-medium">{event.title}</p>
                  <p className="text-sm text-gray-500">{event.category}</p>
                </div>
                <span className={`px-2 py-1 text-xs rounded ${
                  event.status === 'PUBLISHED' ? 'bg-green-100 text-green-800' :
                  event.status === 'DRAFT' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-gray-100 text-gray-800'
                }`}>
                  {event.status}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
