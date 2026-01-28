import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import DashboardCard from "../../components/common/DashboardCard";
import RentalCard from "../../components/common/FarmerRentalCard";
import { getOwnerRentals } from "../../services/ownerService";

export default function OwnerDashboard() {
  const [rentals, setRentals] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    loadRentals();
  }, []);

  const loadRentals = async () => {
    try {
      const res = await getOwnerRentals();
      setRentals(res.data || []);
    } catch (err) {
      console.error("Failed to load owner rentals", err);
    } finally {
      setLoading(false);
    }
  };

  // ================= STATS =================
  const totalRentals = rentals.length;
  const pendingRequests = rentals.filter(
    r => r.status === "PENDING"
  ).length;
  const approvedRentals = rentals.filter(
    r => r.status === "APPROVED"
  ).length;

  // ================= RECENT (LAST 5) =================
  const recentRentals = [...rentals]
    .sort((a, b) => b.rentalId - a.rentalId)
    .slice(0, 3);

  return (
    <div>
      {/* HEADER */}
      <h1 className="text-2xl font-bold mb-1">
        Owner Dashboard 🏭
      </h1>
      <p className="text-gray-500 mb-6">
        Manage your equipments and rental requests
      </p>

      {/* STATS */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
        <DashboardCard title="Total Rentals" value={totalRentals} />
        <DashboardCard title="Pending Requests" value={pendingRequests} />
        <DashboardCard title="Approved Rentals" value={approvedRentals} />
      </div>

      {/* RECENT RENTALS */}
      <div className="bg-white rounded-2xl shadow p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold">
            Recent Rental Requests
          </h2>

          <button
            onClick={() => navigate("/owner/rentals")}
            className="text-green-700 text-sm font-semibold hover:underline"
          >
            View All →
          </button>
        </div>

        {loading ? (
          <p className="text-gray-500">Loading...</p>
        ) : recentRentals.length === 0 ? (
          <p className="text-gray-500">
            No rental requests yet
          </p>
        ) : (
          <div className="grid gap-4">
            {recentRentals.map(rental => (
              <RentalCard
                key={rental.rentalId}
                rental={rental}
                role="OWNER"
                onAction={loadRentals}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
