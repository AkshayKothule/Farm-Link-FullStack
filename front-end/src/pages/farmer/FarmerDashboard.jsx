import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../services/api";

import DashboardCard from "../../components/common/DashboardCard";
import FarmerRentalCard from "../../components/common/FarmerRentalCard";
import PaymentModal from "../../components/modals/PaymentModal";

export default function FarmerDashboard() {
  const [allRentals, setAllRentals] = useState([]);
  const [recentRentals, setRecentRentals] = useState([]);
  const [farmer, setFarmer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedRental, setSelectedRental] = useState(null);

  const navigate = useNavigate();

  // ================= LOAD DASHBOARD =================
  const loadDashboard = async () => {
    try {
      const [rentalsRes, farmerRes] = await Promise.all([
        api.get("/rentals/farmer"),
        api.get("/farmers/profile"),
      ]);

      const rentalsData = Array.isArray(rentalsRes.data)
        ? rentalsRes.data
        : [];
       console.log("renatalsData :"+rentalsData.data);
      // 🔹 ALL rentals → for stats
      setAllRentals(rentalsData);

      // 🔹 RECENT 3 rentals → for list
      const recent = [...rentalsData]
        .sort(
          (a, b) =>
            new Date(b.createdAt || b.startDate) -
            new Date(a.createdAt || a.startDate)
        )
        .slice(0, 3);

      setRecentRentals(recent);
      setFarmer(farmerRes.data);
    } catch (err) {
      console.error("Failed to load farmer dashboard", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadDashboard();
  }, []);

  // ================= STATS =================
  const totalRequests = allRentals.length;
  const approvedCount = allRentals.filter(
    r => r.status === "APPROVED"
  ).length;
  const pendingCount = allRentals.filter(
    r => r.status === "PENDING"
  ).length;

  // ================= PAY =================
  const handlePay = (rental) => {
    setSelectedRental(rental);
  };

  return (
    <>
      {/* HEADER */}
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-green-900">
          Hello {farmer?.firstName || "Farmer"} 👨‍🌾
        </h1>
        <p className="text-gray-600 mt-1">
          Manage your rental requests and payments
        </p>
      </div>

      {/* STATS */}
      <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 mb-10">
        <DashboardCard title="Total Requests" value={totalRequests} />
        <DashboardCard title="Approved Rentals" value={approvedCount} />
        <DashboardCard title="Pending Requests" value={pendingCount} />
      </div>

      {/* RECENT RENTALS */}
      <div className="bg-white rounded-2xl shadow p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold">
            Recent Rental Requests
          </h2>

          <button
            onClick={() => navigate("/farmer/rentals")}
            className="text-green-700 text-sm font-semibold hover:underline"
          >
            View All →
          </button>
        </div>

        {loading ? (
          <p className="text-gray-500 text-sm">Loading...</p>
        ) : recentRentals.length === 0 ? (
          <p className="text-gray-500 text-sm">
            No rental requests yet.
          </p>
        ) : (
          <div className="space-y-4">
            {recentRentals.map(rental => (
              <FarmerRentalCard
                key={rental.rentalId || rental.id}
                rental={rental}
                onPay={handlePay}
                onCancel={loadDashboard}
              />
            ))}
          </div>
        )}
      </div>

      {/* PAYMENT MODAL */}
      {selectedRental && (
        <PaymentModal
          rental={selectedRental}
          onClose={() => setSelectedRental(null)}
          onSuccess={() => {
            setSelectedRental(null);
            loadDashboard();
          }}
        />
      )}
    </>
  );
}
