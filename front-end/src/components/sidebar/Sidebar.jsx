import { useLocation, useNavigate } from "react-router-dom";
import SidebarItem from "./SidebarItem";

export default function Sidebar({ onLogout }) {
  const navigate = useNavigate();
  const location = useLocation();

  const isExact = (path) => location.pathname === path;

  const isNested = (path) =>
    location.pathname === path ||
    location.pathname.startsWith(path + "/");

  return (
    <aside className="w-64 bg-green-900 text-white flex flex-col px-6 py-8">
      <h2 className="text-2xl font-bold mb-10">🌿 FarmLink</h2>

      <nav className="space-y-3 text-sm font-medium">
        {/* ✅ DASHBOARD → exact only */}
        <SidebarItem
          label="Dashboard"
          active={isExact("/farmer")}
          onClick={() => navigate("/farmer")}
        />

        {/* ✅ NESTED ROUTES */}
        <SidebarItem
          label="Browse Equipment"
          active={isNested("/farmer/equipments")}
          onClick={() => navigate("/farmer/equipments")}
        />

        <SidebarItem
          label="My Rentals"
          active={isNested("/farmer/rentals")}
          onClick={() => navigate("/farmer/rentals")}
        />

        <SidebarItem
          label="Payments"
          active={isNested("/farmer/payments")}
          onClick={() => navigate("/farmer/payments")}
        />

        <SidebarItem
          label="Profile"
          active={
            isNested("/farmer/profile")
          }
          onClick={() => navigate("/farmer/profile")}
        />

        <SidebarItem
          label="Settings"
          active={
            isNested("/farmer/settings")
          }
          onClick={() => navigate("/farmer/settings")}
        />
      </nav>

      <button
        onClick={onLogout}
        className="mt-auto text-red-200 hover:text-white text-sm font-semibold"
      >
        ⏻ Logout
      </button>
    </aside>
  );
}
