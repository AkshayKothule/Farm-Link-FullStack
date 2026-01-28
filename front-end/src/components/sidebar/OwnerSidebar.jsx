import { useLocation, useNavigate } from "react-router-dom";
import SidebarItem from "./SidebarItem";

export default function OwnerSidebar({ onLogout }) {
  const navigate = useNavigate();
  const location = useLocation();

  // Dashboard only when exact "/owner"
  const isDashboard = location.pathname === "/owner";

  const isActive = (path) =>
    location.pathname === path ||
    location.pathname.startsWith(path + "/");

  return (
    <aside className="w-64 bg-green-900 text-white flex flex-col px-6 py-8">
      <h2 className="text-2xl font-bold mb-10">
        🏭 FarmLink Owner
      </h2>

      <nav className="space-y-3 text-sm font-medium">
        {/* DASHBOARD */}
        <SidebarItem
          label="Dashboard"
          active={isDashboard}
          onClick={() => navigate("/owner")}
        />

        {/* EQUIPMENTS */}
        <SidebarItem
          label="My Equipments"
          active={isActive("/owner/equipments")}
          onClick={() => navigate("/owner/equipments")}
        />

        {/* RENTALS */}
        <SidebarItem
          label="Rental Requests"
          active={isActive("/owner/rentals")}
          onClick={() => navigate("/owner/rentals")}
        />

        {/* PAYMENTS */}
        <SidebarItem
          label="Payments"
          active={isActive("/owner/payments")}
          onClick={() => navigate("/owner/payments")}
        />

        {/* PROFILE */}
        <SidebarItem
          label="Profile"
          active={isActive("/owner/profile")}
          onClick={() => navigate("/owner/profile")}
        />

        {/* SETTINGS */}
        <SidebarItem
          label="Settings"
          active={isActive("/owner/settings")}
          onClick={() => navigate("/owner/settings")}
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
