import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from './components/shared/ProtectedRoute';
import { DashboardLayout } from './components/layout/DashboardLayout';
import { ToastProvider } from './components/ui/Toast';

// Auth pages
import { StudentLogin } from './pages/auth/StudentLogin';
import { AdminLogin } from './pages/auth/AdminLogin';
import { ChangePassword } from './pages/auth/ChangePassword';

// Student pages
import { StudentDashboard } from './pages/student/Dashboard';
import { EventList } from './pages/student/EventList';
import { EventDetail } from './pages/student/EventDetail';
import { SubmissionPage } from './pages/student/SubmissionPage';
import { Results } from './pages/student/Results';
import { Leaderboard } from './pages/student/Leaderboard';
import { AccountSettings } from './pages/student/AccountSettings';

// Admin pages
import { AdminDashboard } from './pages/admin/Dashboard';
import { AdminManagement } from './pages/admin/AdminManagement';
import { StudentManagement } from './pages/admin/StudentManagement';
import { EventManagement } from './pages/admin/EventManagement';
import { EventBuilder } from './pages/admin/EventBuilder';
import { SubmissionReview } from './pages/admin/SubmissionReview';
import { LeaderboardAdmin } from './pages/admin/LeaderboardAdmin';

function App() {
  return (
    <ToastProvider>
      <Router>
        <Routes>
          {/* Public routes */}
          <Route path="/login" element={<StudentLogin />} />
          <Route path="/admin/login" element={<AdminLogin />} />
          <Route path="/change-password" element={<ChangePassword />} />

          {/* Student routes */}
          <Route
            path="/student"
            element={
              <ProtectedRoute requiredRole="STUDENT">
                <DashboardLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<StudentDashboard />} />
            <Route path="events" element={<EventList />} />
            <Route path="events/:id" element={<EventDetail />} />
            <Route path="submissions/:id" element={<SubmissionPage />} />
            <Route path="results" element={<Results />} />
            <Route path="leaderboard" element={<Leaderboard />} />
            <Route path="settings" element={<AccountSettings />} />
          </Route>

          {/* Admin routes */}
          <Route
            path="/admin"
            element={
              <ProtectedRoute requiredRole="ADMIN">
                <DashboardLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<AdminDashboard />} />
            <Route path="admins" element={<AdminManagement />} />
            <Route path="students" element={<StudentManagement />} />
            <Route path="events" element={<EventManagement />} />
            <Route path="events/new" element={<EventBuilder />} />
            <Route path="events/:id" element={<EventBuilder />} />
            <Route path="events/:eventId/submissions" element={<SubmissionReview />} />
            <Route path="leaderboard" element={<LeaderboardAdmin />} />
          </Route>

          {/* Default redirect */}
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="*" element={<Navigate to="/login" replace />} />
        </Routes>
      </Router>
    </ToastProvider>
  );
}

export default App;
