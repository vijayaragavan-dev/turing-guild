import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import {
  LayoutDashboard, Users, Calendar, Trophy,
  ClipboardList, Settings
} from 'lucide-react';

export function Sidebar() {
  const { role } = useAuth();
  const location = useLocation();

  const studentLinks = [
    { to: '/student', icon: LayoutDashboard, label: 'Dashboard' },
    { to: '/student/events', icon: Calendar, label: 'Events' },
    { to: '/student/results', icon: ClipboardList, label: 'My Results' },
    { to: '/student/leaderboard', icon: Trophy, label: 'Leaderboard' },
    { to: '/student/settings', icon: Settings, label: 'Account Settings' },
  ];

  const adminLinks = [
    { to: '/admin', icon: LayoutDashboard, label: 'Dashboard' },
    { to: '/admin/students', icon: Users, label: 'Students' },
    { to: '/admin/events', icon: Calendar, label: 'Events' },
    { to: '/admin/leaderboard', icon: Trophy, label: 'Leaderboard' },
  ];

  const links = role === 'ADMIN' ? adminLinks : studentLinks;

  return (
    <aside className="w-60 bg-white/60 backdrop-blur-sm border-r border-gray-200/60 min-h-[calc(100vh-3.5rem)]">
      <nav className="mt-4">
        <ul className="space-y-1 px-3">
          {links.map((link) => {
            const isActive = location.pathname === link.to ||
              (link.to !== '/admin' && link.to !== '/student' && location.pathname.startsWith(link.to));
            return (
              <li key={link.to}>
                <Link
                  to={link.to}
                  className={`flex items-center gap-3 px-3 py-2.5 rounded-xl text-sm font-medium transition-all duration-200 ${
                    isActive
                      ? 'bg-slate-900 text-white shadow-md shadow-slate-200/50'
                      : 'text-gray-600 hover:bg-gray-100/80 hover:text-gray-900'
                  }`}
                >
                  <link.icon size={18} className={isActive ? 'text-white' : 'text-gray-400'} />
                  <span>{link.label}</span>
                </Link>
              </li>
            );
          })}
        </ul>
      </nav>
    </aside>
  );
}
