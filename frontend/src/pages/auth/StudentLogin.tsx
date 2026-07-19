import { useState, useRef, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { useToast } from '../../components/ui/Toast';
import { AuthLayout } from '../../components/layout/AuthLayout';
import { Eye, EyeOff, ArrowRight, Loader2 } from 'lucide-react';
import { motion } from 'framer-motion';

export function StudentLogin() {
  const [batchNumber, setBatchNumber] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const { toast } = useToast();
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    inputRef.current?.focus();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const trimmedBatch = batchNumber.trim();
    if (!trimmedBatch) {
      setError('Batch number is required');
      setLoading(false);
      return;
    }

    try {
      const response = await login(trimmedBatch, password);
      toast('Welcome back!', 'success');
      if (response.role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate('/student');
      }
    } catch {
      setError('Invalid batch number or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <AuthLayout>
      <div className="text-center mb-8">
        <h1 className="text-2xl font-semibold text-gray-900 tracking-tight">
          Student Login
        </h1>
        <p className="mt-2 text-sm text-gray-500">
          Sign in with your batch number to continue
        </p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-5">
        {error && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-xl"
          >
            {error}
          </motion.div>
        )}

        <div className="space-y-1.5">
          <label htmlFor="batchNumber" className="block text-sm font-medium text-gray-700">
            Batch Number
          </label>
          <input
            ref={inputRef}
            id="batchNumber"
            type="text"
            autoComplete="username"
            value={batchNumber}
            onChange={(e) => setBatchNumber(e.target.value)}
            className="w-full px-4 py-2.5 bg-gray-50/80 border border-gray-200 rounded-xl text-sm text-gray-900 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-slate-900/10 focus:border-slate-300 transition-all duration-200"
          />
        </div>

        <div className="space-y-1.5">
          <label htmlFor="password" className="block text-sm font-medium text-gray-700">
            Password
          </label>
          <div className="relative">
            <input
              id="password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              className="w-full px-4 py-2.5 bg-gray-50/80 border border-gray-200 rounded-xl text-sm text-gray-900 placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-slate-900/10 focus:border-slate-300 transition-all duration-200 pr-10"
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors"
              tabIndex={-1}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full flex items-center justify-center gap-2 py-2.5 px-4 bg-gradient-to-r from-slate-800 to-slate-900 hover:from-slate-700 hover:to-slate-800 text-white text-sm font-medium rounded-xl shadow-lg shadow-slate-200/50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-900 disabled:opacity-50 disabled:cursor-not-allowed transition-all duration-200"
        >
          {loading ? (
            <Loader2 className="w-4 h-4 animate-spin" />
          ) : (
            <>
              Sign In
              <ArrowRight className="w-4 h-4" />
            </>
          )}
        </button>
      </form>

      <div className="mt-6 pt-6 border-t border-gray-100 text-center">
        <Link
          to="/admin/login"
          className="text-sm text-gray-500 hover:text-gray-800 transition-colors"
        >
          Admin Login
        </Link>
      </div>
    </AuthLayout>
  );
}
