import { useEffect, useState } from 'react';
import { studentsApi } from '../../api/students';
import { useToast } from '../../components/ui/Toast';
import type { Student } from '../../types';
import { Plus, Edit, Trash2, Key } from 'lucide-react';

export function StudentManagement() {
  const [students, setStudents] = useState<Student[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingStudent, setEditingStudent] = useState<Student | null>(null);
  const [resetModal, setResetModal] = useState<Student | null>(null);
  const [resetLoading, setResetLoading] = useState(false);
  const [formData, setFormData] = useState({
    batchNumber: '',
    email: '',
    fullName: '',
    password: '',
  });
  const { toast } = useToast();

  useEffect(() => {
    fetchStudents();
  }, []);

  const fetchStudents = async () => {
    try {
      const data = await studentsApi.getAllStudents();
      setStudents(data);
    } catch (error) {
      console.error('Failed to fetch students:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingStudent) {
        await studentsApi.updateStudent(editingStudent.id, {
          email: formData.email,
          fullName: formData.fullName,
        });
      } else {
        await studentsApi.createStudent(formData);
      }
      setShowModal(false);
      setEditingStudent(null);
      setFormData({ batchNumber: '', email: '', fullName: '', password: '' });
      fetchStudents();
    } catch (error) {
      console.error('Failed to save student:', error);
    }
  };

  const handleEdit = (student: Student) => {
    setEditingStudent(student);
    setFormData({
      batchNumber: student.batchNumber,
      email: student.email,
      fullName: student.fullName,
      password: '',
    });
    setShowModal(true);
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to deactivate this student?')) {
      try {
        await studentsApi.deleteStudent(id);
        toast('Student deactivated successfully', 'success');
        fetchStudents();
      } catch (error) {
        toast('Failed to deactivate student', 'error');
        console.error('Failed to delete student:', error);
      }
    }
  };

  const handleResetPassword = (student: Student) => {
    setResetModal(student);
  };

  const confirmResetPassword = async () => {
    if (!resetModal) return;
    setResetLoading(true);
    try {
      const name = resetModal.fullName || '';
      const batch = resetModal.batchNumber || '';
      const prefix = name.length >= 4 ? name.substring(0, 4) : name;
      const defaultPassword = prefix + batch + '$';

      await studentsApi.resetPassword(resetModal.id);
      toast(`Password reset successfully. Default password: ${defaultPassword}`, 'success');
      setResetModal(null);
    } catch (error) {
      toast('Failed to reset password', 'error');
      console.error('Failed to reset password:', error);
    } finally {
      setResetLoading(false);
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
        <h1 className="text-2xl font-bold text-gray-900">Student Management</h1>
        <button
          onClick={() => {
            setEditingStudent(null);
            setFormData({ batchNumber: '', email: '', fullName: '', password: '' });
            setShowModal(true);
          }}
          className="flex items-center space-x-2 px-4 py-2 bg-primary text-white rounded-lg hover:bg-primary/90"
        >
          <Plus size={16} />
          <span>Add Student</span>
        </button>
      </div>

      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Batch Number
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Name
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Email
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Status
              </th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Actions
              </th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {students.map((student) => (
              <tr key={student.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {student.batchNumber}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {student.fullName}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {student.email}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`px-2 py-1 text-xs rounded ${student.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                    {student.isActive ? 'Active' : 'Inactive'}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm space-x-2">
                  <button onClick={() => handleEdit(student)} className="text-blue-600 hover:text-blue-900">
                    <Edit size={16} />
                  </button>
                  <button onClick={() => handleResetPassword(student)} className="text-yellow-600 hover:text-yellow-900">
                    <Key size={16} />
                  </button>
                  <button onClick={() => handleDelete(student.id)} className="text-red-600 hover:text-red-900">
                    <Trash2 size={16} />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h2 className="text-lg font-semibold mb-4">
              {editingStudent ? 'Edit Student' : 'Add Student'}
            </h2>
            <form onSubmit={handleSubmit} className="space-y-4">
              {!editingStudent && (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Batch Number</label>
                  <input
                    type="text"
                    required
                    value={formData.batchNumber}
                    onChange={(e) => setFormData({ ...formData, batchNumber: e.target.value })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md"
                  />
                </div>
              )}
              <div>
                <label className="block text-sm font-medium text-gray-700">Full Name</label>
                <input
                  type="text"
                  required
                  value={formData.fullName}
                  onChange={(e) => setFormData({ ...formData, fullName: e.target.value })}
                  className="mt-1 block w-full px-3 py-2 border rounded-md"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700">Email</label>
                <input
                  type="email"
                  required
                  value={formData.email}
                  onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                  className="mt-1 block w-full px-3 py-2 border rounded-md"
                />
              </div>
              {!editingStudent && (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Password</label>
                  <input
                    type="password"
                    required
                    value={formData.password}
                    onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                    className="mt-1 block w-full px-3 py-2 border rounded-md"
                  />
                </div>
              )}
              <div className="flex justify-end space-x-3 mt-6">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="px-4 py-2 border rounded-md hover:bg-gray-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="px-4 py-2 bg-primary text-white rounded-md hover:bg-primary/90"
                >
                  {editingStudent ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {resetModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h2 className="text-lg font-semibold mb-2">Reset Password</h2>
            <p className="text-sm text-gray-600 mb-4">
              Are you sure you want to reset the password for{' '}
              <span className="font-medium text-gray-900">{resetModal.fullName}</span>{' '}
              ({resetModal.batchNumber})?
            </p>
            <div className="bg-amber-50 border border-amber-200 rounded-lg p-3 mb-4">
              <p className="text-sm text-amber-800">
                The student will be able to log in with the default password:{' '}
                <span className="font-mono font-medium">
                  {(() => {
                    const name = resetModal.fullName || '';
                    const batch = resetModal.batchNumber || '';
                    return (name.length >= 4 ? name.substring(0, 4) : name) + batch + '$';
                  })()}
                </span>
              </p>
            </div>
            <div className="flex justify-end space-x-3">
              <button
                onClick={() => setResetModal(null)}
                disabled={resetLoading}
                className="px-4 py-2 border rounded-md hover:bg-gray-50 disabled:opacity-50"
              >
                Cancel
              </button>
              <button
                onClick={confirmResetPassword}
                disabled={resetLoading}
                className="px-4 py-2 bg-amber-600 text-white rounded-md hover:bg-amber-700 disabled:opacity-50"
              >
                {resetLoading ? 'Resetting...' : 'Reset Password'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
