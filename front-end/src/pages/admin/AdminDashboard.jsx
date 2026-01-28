import { useEffect, useState } from "react";
import { getAdminDashboard } from "../../services/adminService";
import { errorToast } from "../../utils/toast";

import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  ResponsiveContainer,
  BarChart,
  Bar,
  XAxis,
  YAxis,
} from "recharts";

export default function AdminDashboard() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAdminDashboard()
      .then(res => setData(res.data))
      .catch(() => errorToast("Failed to load dashboard"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return (
      <p className="text-gray-600 font-medium">
        Loading admin dashboard...
      </p>
    );
  }

  if (!data) return null;

  /* ================= SUMMARY CARDS ================= */
  const cards = [
    { title: "Total Farmers", value: data.totalFarmers, color: "text-blue-600" },
    { title: "Total Owners", value: data.totalOwners, color: "text-green-600" },
    { title: "Pending Owners", value: data.pendingOwners, color: "text-orange-600" },
    { title: "Total Equipments", value: data.totalEquipments, color: "text-indigo-600" },
    { title: "Total Rentals", value: data.totalRentals, color: "text-purple-600" },
    { title: "Total Reviews", value: data.totalReviews, color: "text-red-600" },
  ];

  /* ================= CHART DATA ================= */
  const userPieData = [
    { name: "Farmers", value: data.totalFarmers },
    { name: "Owners", value: data.totalOwners },
  ];

  const barData = [
    { name: "Equipments", value: data.totalEquipments },
    { name: "Rentals", value: data.totalRentals },
  ];

  const COLORS = ["#2563eb", "#16a34a"];

  const verifiedOwners = data.totalOwners - data.pendingOwners;
  const verifyPercent =
    data.totalOwners > 0
      ? Math.round((verifiedOwners / data.totalOwners) * 100)
      : 0;

  /* ================= INSIGHTS ================= */
  const insight =
    data.pendingOwners > 5
      ? "⚠️ Many owners pending verification"
      : "✅ Platform is healthy";

  return (
    <div className="space-y-8">

      {/* ===== HEADER ===== */}
      <h1 className="text-2xl font-bold">
        Admin Dashboard 📊
      </h1>

      {/* ===== SUMMARY CARDS ===== */}
      <div
        className="
          grid gap-6
          grid-cols-1
          sm:grid-cols-2
          lg:grid-cols-3
          xl:grid-cols-6
        "
      >
        {cards.map((c, i) => (
          <div
            key={i}
            className="bg-white rounded-2xl shadow p-5"
          >
            <h3 className="text-sm text-gray-600">
              {c.title}
            </h3>
            <p className={`text-3xl font-bold mt-2 ${c.color}`}>
              {c.value}
            </p>
          </div>
        ))}
      </div>

      {/* ===== CHARTS ===== */}
      <div className="grid gap-6 lg:grid-cols-2">

        {/* USER DISTRIBUTION */}
        <div className="bg-white rounded-2xl shadow p-5 h-80">
          <h3 className="font-semibold mb-4">
            User Distribution
          </h3>

          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={userPieData}
                dataKey="value"
                innerRadius={60}
                outerRadius={90}
              >
                {userPieData.map((_, i) => (
                  <Cell key={i} fill={COLORS[i]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        {/* EQUIPMENT vs RENTALS */}
        <div className="bg-white rounded-2xl shadow p-5 h-80">
          <h3 className="font-semibold mb-4">
            Equipments vs Rentals
          </h3>

          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={barData}>
              <XAxis dataKey="name" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="value" fill="#16a34a" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* ===== OWNER VERIFICATION ===== */}
      <div className="bg-white rounded-2xl shadow p-6">
        <h3 className="font-semibold mb-3">
          Owner Verification Status
        </h3>

        <div className="w-full bg-gray-200 rounded-full h-3">
          <div
            className="bg-green-600 h-3 rounded-full"
            style={{ width: `${verifyPercent}%` }}
          />
        </div>

        <p className="text-sm text-gray-600 mt-2">
          {verifiedOwners} / {data.totalOwners} verified ({verifyPercent}%)
        </p>
      </div>

      {/* ===== INSIGHT ===== */}
      <div className="bg-green-50 border border-green-200 rounded-xl p-4">
        <p className="font-medium text-green-800">
          {insight}
        </p>
      </div>

    </div>
  );
}
