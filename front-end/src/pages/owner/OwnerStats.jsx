import { useEffect, useState } from "react";
import axios from "axios";

export default function OwnerStats() {
  const [equipments, setEquipments] = useState([]);
  const [rentalsRaw, setRentalsRaw] = useState([]); // 👈 raw response

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const eqRes = await axios.get("/owners/equipments");
      setEquipments(Array.isArray(eqRes.data) ? eqRes.data : []);
    } catch {
      setEquipments([]);
    }try {
  const rentRes = await axios.get("/rentals/owner");
  console.log("RAW RESPONSE 👉", rentRes);
  console.log("RESPONSE DATA 👉", rentRes.data);
  setRentalsRaw(rentRes.data);
} catch (err) {
  console.error("ERROR 👉", err);
  setRentalsRaw([]);
}


    
  };

  // ✅ ALWAYS ARRAY — no matter what backend / interceptor does
  const rentals = Array.isArray(rentalsRaw)
    ? rentalsRaw
    : Array.isArray(rentalsRaw?.data)
    ? rentalsRaw.data
    : [];

  const pending = rentals.filter(r => r.status === "PENDING").length;

  return (
    <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
      <Stat title="Total Equipments" value={equipments.length} />
      <Stat title="Total Rentals" value={rentals.length} />
      <Stat title="Pending Requests" value={pending} />
    </div>
  );
}

function Stat({ title, value }) {
  return (
    <div className="bg-white rounded-xl shadow p-6">
      <p className="text-gray-500">{title}</p>
      <h2 className="text-3xl font-bold text-green-700">{value}</h2>
    </div>
  );
}
