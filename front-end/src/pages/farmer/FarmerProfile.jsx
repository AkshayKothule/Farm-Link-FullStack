import { useEffect, useState } from "react";
import api from "../../services/api";
import Sidebar from "../../components/sidebar/Sidebar";
import EditFarmerProfileModal from "../../components/modals/EditFarmerProfileModal";
import InfoRow from "../../components/common/InfoRow";

export default function FarmerProfile() {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [isEditOpen, setIsEditOpen] = useState(false);

  // ================= LOAD PROFILE =================
  const loadProfile = () => {
    setLoading(true);
    api
      .get("/farmers/profile")
      .then((res) => setProfile(res.data))
      .catch(() => setError("Failed to load profile"))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadProfile();
  }, []);

  // ================= UI STATES =================
  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center text-green-700 font-semibold">
        Loading profile...
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex items-center justify-center text-red-600 font-semibold">
        {error}
      </div>
    );
  }

  return (
    <div className="min-h-screen flex bg-green-50">
    

      {/* MAIN */}
      {/* MAIN */}
<main className="flex-1 p-6 sm:p-8">
  {/* HEADER */}
  <div className="mb-8 flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
    <div>
      <h1 className="text-3xl font-bold text-green-900">
        My Profile 🌾
      </h1>
      <p className="text-gray-600 mt-1">
        View and manage your personal & farm information
      </p>
    </div>

    <button
      onClick={() => setIsEditOpen(true)}
      className="
        bg-green-600 text-white px-6 py-2 rounded-xl
        font-semibold hover:bg-green-700 transition
        shadow-sm
      "
    >
      ✏️ Edit Profile
    </button>
  </div>

  {/* PROFILE CARDS */}
  <div className="grid gap-6 lg:grid-cols-2 max-w-5xl">

    {/* PERSONAL DETAILS */}
    <div className="bg-white rounded-2xl shadow p-6">
      <h2 className="text-lg font-semibold text-green-900 mb-4 flex items-center gap-2">
        👤 Personal Details
      </h2>

      <div className="space-y-3 text-sm">
        <InfoRow label="First Name" value={profile.firstName} />
        <InfoRow label="Last Name" value={profile.lastName} />
        <InfoRow label="Phone Number" value={profile.phoneNumber} />
      </div>
    </div>

    {/* FARM DETAILS */}
    <div className="bg-white rounded-2xl shadow p-6">
      <h2 className="text-lg font-semibold text-green-900 mb-4 flex items-center gap-2">
        🌱 Farm Details
      </h2>

      <div className="space-y-3 text-sm">
        <InfoRow label="Farm Type" value={profile.farmType} />
        <InfoRow
          label="Land Area"
          value={`${profile.landArea} acres`}
        />
      </div>
    </div>
  </div>
</main>


      {/* EDIT MODAL */}
      {isEditOpen && (
        <EditFarmerProfileModal
          profile={profile}
          onClose={() => setIsEditOpen(false)}
          onSuccess={() => {
            setIsEditOpen(false);
            loadProfile();
          }}
        />
      )}
    </div>
  );
}
