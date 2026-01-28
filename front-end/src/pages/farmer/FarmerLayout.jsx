import { Outlet, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";

import Sidebar from "../../components/sidebar/Sidebar";
import { logout } from "../../redux/slices/authSlice";

export default function FarmerLayout() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    localStorage.clear();
    navigate("/auth", { replace: true });
  };

  return (
    // ✅ Full viewport height, no body scroll
    <div className="h-screen flex overflow-hidden bg-green-50">
      
      {/* ✅ SIDEBAR (stable) */}
      <Sidebar onLogout={handleLogout} />

      {/* ✅ ONLY THIS SCROLLS */}
      <main className="flex-1 overflow-y-auto p-6 sm:p-8">
        <Outlet />
      </main>

    </div>
  );
}
