import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { useToast } from '../ui/Toast';
import { LogOut, User, Shield } from 'lucide-react';

export function Navbar() {
  const { isAuthenticated, role, username, logout } = useAuth();
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleLogout = () => {
    logout();
    toast('Signed out successfully', 'success');
    navigate('/login');
  };

  return (
    <nav className="bg-white/80 backdrop-blur-md border-b border-gray-200/60 sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-14">
          <div className="flex items-center">
            <Link to="/" className="flex-shrink-0 flex items-center gap-2">
              <div className="w-7 h-7 bg-gradient-to-br from-slate-700 to-slate-900 rounded-lg flex items-center justify-center">
                <Shield className="w-4 h-4 text-white" />
              </div>
              <span className="text-base font-semibold text-slate-800 tracking-tight">TGMS</span>
            </Link>
          </div>

          <div className="flex items-center space-x-3">
            {isAuthenticated ? (
              <>
                <div className="flex items-center gap-2 px-3 py-1.5 bg-gray-50 rounded-lg border border-gray-100">
                  <User size={14} className="text-gray-400" />
                  <span className="text-sm text-gray-600 max-w-[120px] truncate">{username}</span>
                  <span className="px-1.5 py-0.5 bg-slate-100 text-slate-600 rounded text-xs font-medium">
                    {role}
                  </span>
                </div>
                <button
                  onClick={handleLogout}
                  className="flex items-center gap-1.5 px-3 py-1.5 text-sm text-gray-500 hover:text-red-600 hover:bg-red-50 rounded-lg transition-all duration-200"
                >
                  <LogOut size={14} />
                  <span className="hidden sm:inline">Logout</span>
                </button>
              </>
            ) : (
              <div className="space-x-3">
                <Link to="/login" className="text-sm text-gray-500 hover:text-gray-800 transition-colors">
                  Login
                </Link>
              </div>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
}
