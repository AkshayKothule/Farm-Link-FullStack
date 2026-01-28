import api from "./api";

// ================= RENTALS =================

export const getOwnerRentals = () =>
  api.get("/rentals/owner");

export const approveRental = (id) =>
  api.put(`/rentals/owner/${id}/approve`);

export const rejectRental = (id) =>
  api.put(`/rentals/owner/${id}/reject`);

// ================= OWNER PROFILE =================

export const createOwnerProfile = (payload) =>
  api.post("/owners/profile", payload);

export const getOwnerProfile = () =>
  api.get("/owners/profile");

export const updateOwnerProfile = (payload) =>
  api.put("/owners/profile", payload);

// ================= DASHBOARD =================

export const getOwnerDashboardStats = () =>
  api.get("/owner");



export const getOwnerPayments = () => {
  return api.get("/payments/owner");
};
