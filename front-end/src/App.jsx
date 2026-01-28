import { Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

import LandingPage from "./pages/LandingPage";
import AuthPage from "./pages/AuthPage";

import FarmerLayout from "./pages/farmer/FarmerLayout";
import FarmerDashboard from "./pages/farmer/FarmerDashboard";
import BrowseEquipments from "./pages/farmer/BrowseEquipments";
import FarmerProfile from "./pages/farmer/FarmerProfile";
import MyRentals from "./pages/farmer/MyRentals.jsx";
import PaymentHistory from "./pages/farmer/PaymentHistory";
import Settings from "./pages/farmer/Settings.jsx";

import OwnerDashboard from "./pages/owner/OwnerDashboard";
import OwnerLayout from "./pages/owner/OwnerLayout";
import MyEquipments from "./pages/owner/MyEquipments";
import OwnerProfile from "./pages/owner/OwnerProfile";
import OwnerRentals from "./pages/owner/OwnerRentals.jsx";
import OwnerPayments from "./pages/owner/OwnerPayments.jsx";
import ResetPassword from "./pages/ResetPassword";
import OwnerSettings from "./pages/owner/OwnerSettings.jsx";
import { ToastContainer } from "react-toastify";
import AdminLayout from "./pages/admin/AdminLayout.jsx";
import AdminDashboard from "./pages/admin/AdminDashboard.jsx";
import AdminOwners from "./pages/admin/AdminOwners.jsx";
import AdminReviews from "./pages/admin/AdminReviews.jsx";
import AdminFarmers from "./pages/admin/AdminFarmers.jsx";
// path तुझ्या project structure नुसार adjust कर

// import OwnerPayments from "./pages/owner/"
function App() {
  const { isAuthenticated, role } = useSelector((state) => state.auth);

  const isFarmer =
    isAuthenticated && (role === "FARMER" || role === "ROLE_FARMER");

  const isOwner =
    isAuthenticated && (role === "OWNER" || role === "ROLE_OWNER");

  const isAdmin =
    isAuthenticated && (role === "ADMIN" || role === "ROLE_ADMIN");

  return (
    <>
      <Routes>
        {/* PUBLIC */}
        <Route path="/" element={<LandingPage />} />
        <Route path="/auth" element={<AuthPage />} />
        <Route path="/reset-password" element={<ResetPassword />} />

        {/* 🌾 FARMER */}
        <Route
          path="/farmer"
          element={isFarmer ? <FarmerLayout /> : <Navigate to="/auth" />}
        >
          <Route index element={<FarmerDashboard />} />
          <Route path="equipments" element={<BrowseEquipments />} />
          <Route path="profile" element={<FarmerProfile />} />
          <Route path="rentals" element={<MyRentals />} />
          <Route path="payments" element={<PaymentHistory />} />
          <Route path="settings" element={<Settings />} />
        </Route>

        {/* 🏭 OWNER */}
        <Route
          path="/owner"
          element={isOwner ? <OwnerLayout /> : <Navigate to="/auth" />}
        >
          <Route index element={<OwnerDashboard />} />
          <Route path="equipments" element={<MyEquipments />} />
          <Route path="rentals" element={<OwnerRentals />} />
          <Route path="payments" element={<OwnerPayments />} />
          <Route path="profile" element={<OwnerProfile />} />
          <Route path="settings" element={<OwnerSettings />} />
        </Route>

        {/* 🛡 ADMIN */}
        <Route
          path="/admin"
          element={isAdmin ? <AdminLayout /> : <Navigate to="/auth" />}
        >
          <Route index element={<AdminDashboard />} />
          <Route path="owners" element={<AdminOwners />} />
          <Route path="reviews" element={<AdminReviews />} />
            <Route path="farmers" element={<AdminFarmers />} /> {/* ✅ */}

        </Route>
      </Routes>

      {/* 🌈 GLOBAL TOAST */}
      <ToastContainer
        position="top-right"
        autoClose={2500}
        newestOnTop
        pauseOnHover
        draggable
        limit={3}
        theme="light"
      />
    </>
  );
}


export default App;
