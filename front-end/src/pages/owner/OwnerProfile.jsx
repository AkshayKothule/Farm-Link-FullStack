import { useEffect, useState } from "react";
import { getOwnerProfile, updateOwnerProfile } from "../../services/ownerService";
import InfoRow from "../../components/common/InfoRow";

export default function OwnerProfile() {

  const [profile, setProfile] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    businessName: ""
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const res = await getOwnerProfile();
      setProfile(res.data);
      setForm({
        firstName: res.data.firstName || "",
        lastName: res.data.lastName || "",
        phoneNumber: res.data.phoneNumber || "",
        businessName: res.data.businessName || ""
      });
    } catch (err) {
      console.error("Failed to load owner profile", err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateOwnerProfile(form);
      setEditMode(false);
      loadProfile();
    } catch (err) {
      console.error("Profile update failed", err);
    }
  };

  if (loading) return <p>Loading...</p>;
  if (!profile) return <p>No profile found</p>;

  return (
    <div className="max-w-2xl">

      {/* HEADER */}
      <h1 className="text-2xl font-bold mb-4">Owner Profile</h1>

      {!editMode ? (
        <>
          {/* VIEW MODE */}
          <div className="bg-white p-6 rounded shadow space-y-3">
            <InfoRow label="First Name" value={profile.firstName} />
            <InfoRow label="Last Name" value={profile.lastName} />
            <InfoRow label="Phone Number" value={profile.phoneNumber} />
            <InfoRow label="Business Name" value={profile.businessName} />
            <InfoRow
              label="Verified"
              value={profile.verified ? "Yes" : "No"}
            />
          </div>

          <button
            onClick={() => setEditMode(true)}
            className="mt-4 px-4 py-2 bg-green-700 text-white rounded"
          >
            Edit Profile
          </button>
        </>
      ) : (
        <>
          {/* EDIT MODE */}
          <form
            onSubmit={handleSubmit}
            className="bg-white p-6 rounded shadow space-y-4"
          >
            <input
              name="firstName"
              value={form.firstName}
              onChange={handleChange}
              placeholder="First Name"
              className="w-full p-2 border rounded"
            />

            <input
              name="lastName"
              value={form.lastName}
              onChange={handleChange}
              placeholder="Last Name"
              className="w-full p-2 border rounded"
            />

            <input
              name="phoneNumber"
              value={form.phoneNumber}
              onChange={handleChange}
              placeholder="Phone Number"
              className="w-full p-2 border rounded"
            />

            <input
              name="businessName"
              value={form.businessName}
              onChange={handleChange}
              placeholder="Business Name"
              className="w-full p-2 border rounded"
            />

            <div className="flex gap-3">
              <button
                type="submit"
                className="px-4 py-2 bg-green-700 text-white rounded"
              >
                Save
              </button>

              <button
                type="button"
                onClick={() => setEditMode(false)}
                className="px-4 py-2 bg-gray-400 text-white rounded"
              >
                Cancel
              </button>
            </div>
          </form>
        </>
      )}

    </div>
  );
}
