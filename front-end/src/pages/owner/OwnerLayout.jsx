import { Outlet, useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { logout } from "../../redux/slices/authSlice";
import Sidebar from "../../components/sidebar/OwnerSidebar";

export default function OwnerLayout() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    localStorage.clear();
    navigate("/auth", { replace: true });
  };

  return (
    // ✅ Full viewport height, body scroll disabled
    <div className="h-screen flex overflow-hidden bg-green-50">

      {/* ✅ FIXED SIDEBAR */}
      <Sidebar onLogout={handleLogout} />

      {/* ✅ ONLY MAIN CONTENT SCROLLS */}
      <main className="flex-1 overflow-y-auto p-6 sm:p-8">
        <Outlet />
      </main>

    </div>
  );
}
