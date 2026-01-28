// pages/admin/AdminLayout.jsx
import { Outlet, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logout } from "../../redux/slices/authSlice";

export default function AdminLayout() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    localStorage.clear();
    navigate("/auth", { replace: true });
  };

  return (
    <div className="min-h-screen flex bg-gray-100">
      {/* SIDEBAR */}
      <aside className="w-64 bg-gray-900 text-white p-6 flex flex-col">
        <h2 className="text-xl font-bold mb-8">🛡 Admin Panel</h2>

        <nav className="space-y-3">
          <button onClick={() => navigate("/admin")} className="block w-full text-left">
            Dashboard
          </button>
          <button onClick={() => navigate("/admin/owners")} className="block w-full text-left">
            Owners
          </button>
           <button onClick={() => navigate("/admin/farmers")} className="block w-full text-left">
            Farmers
          </button>
          <button onClick={() => navigate("/admin/reviews")} className="block w-full text-left">
            Reviews
          </button>
        </nav>

        <button
          onClick={handleLogout}
          className="mt-auto text-red-400 hover:text-white"
        >
          Logout
        </button>
      </aside>

      {/* CONTENT */}
      <main className="flex-1 p-6">
        <Outlet />
      </main>
    </div>
  );
}
