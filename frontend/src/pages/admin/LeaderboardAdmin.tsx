import { useEffect, useState } from 'react';
import { submissionsApi } from '../../api/submissions';
import { eventsApi } from '../../api/events';
import type { Event, LeaderboardEntry } from '../../types';
import { Trophy, RefreshCw } from 'lucide-react';

export function LeaderboardAdmin() {
  const [events, setEvents] = useState<Event[]>([]);
  const [selectedEventId, setSelectedEventId] = useState<number | null>(null);
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([]);
  const [loading, setLoading] = useState(true);
  const [fetchingLeaderboard, setFetchingLeaderboard] = useState(false);
  const [recomputing, setRecomputing] = useState(false);

  useEffect(() => {
    const fetchEvents = async () => {
      try {
        const data = await eventsApi.getAllEvents();
        setEvents(data);
        if (data.length > 0) {
          setSelectedEventId(data[0].id);
        }
      } catch (error) {
        console.error('Failed to fetch events:', error);
      } finally {
        setLoading(false);
      }
    };
    fetchEvents();
  }, []);

  useEffect(() => {
    const fetchLeaderboard = async () => {
      if (!selectedEventId) return;
      
      setFetchingLeaderboard(true);
      try {
        const data = await submissionsApi.getLeaderboard(selectedEventId);
        setLeaderboard(data);
      } catch (error) {
        console.error('Failed to fetch leaderboard:', error);
      } finally {
        setFetchingLeaderboard(false);
      }
    };
    fetchLeaderboard();
  }, [selectedEventId]);

  const handleRecompute = async () => {
    if (!selectedEventId) return;
    
    setRecomputing(true);
    try {
      await submissionsApi.recomputeLeaderboard(selectedEventId);
      const data = await submissionsApi.getLeaderboard(selectedEventId);
      setLeaderboard(data);
    } catch (error) {
      console.error('Failed to recompute leaderboard:', error);
    } finally {
      setRecomputing(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
      </div>
    );
  }

  const getRankIcon = (rank: number) => {
    switch (rank) {
      case 1:
        return <Trophy className="text-yellow-500" size={20} />;
      case 2:
        return <span className="text-gray-400 font-bold">2</span>;
      case 3:
        return <span className="text-amber-600 font-bold">3</span>;
      default:
        return <span className="text-gray-500">{rank}</span>;
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-gray-900">Leaderboard Management</h1>

      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex items-center space-x-4">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-2">Select Event</label>
            <select
              value={selectedEventId || ''}
              onChange={(e) => setSelectedEventId(parseInt(e.target.value))}
              className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
            >
              {events.map((event) => (
                <option key={event.id} value={event.id}>
                  {event.title} ({event.category})
                </option>
              ))}
            </select>
          </div>
          <button
            onClick={handleRecompute}
            disabled={recomputing || !selectedEventId}
            className="flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90 disabled:opacity-50 mt-6"
          >
            <RefreshCw size={16} className={recomputing ? 'animate-spin' : ''} />
            <span>Recompute</span>
          </button>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        {fetchingLeaderboard ? (
          <div className="flex items-center justify-center h-32">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
          </div>
        ) : leaderboard.length === 0 ? (
          <div className="text-center py-12">
            <Trophy className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No results yet</h3>
            <p className="mt-1 text-sm text-gray-500">No submissions have been evaluated for this event.</p>
          </div>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Rank
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Student
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Batch
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Score
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {leaderboard.map((entry) => (
                <tr key={entry.userId} className={entry.rank <= 3 ? 'bg-yellow-50' : ''}>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      {getRankIcon(entry.rank)}
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {entry.userFullName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {entry.userBatchNumber}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {entry.totalScore}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
