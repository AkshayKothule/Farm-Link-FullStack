import api from "./api";

// ================= DASHBOARD =================
export const getAdminDashboard = () =>
  api.get("/admin/dashboard");

// ================= OWNERS =================
export const getOwners = () =>
  api.get("/admin/owners");

export const getFarmers = () =>
    api.get("/admin/farmers");

export const verifyOwner = (ownerId) =>
  api.put(`/admin/owners/${ownerId}/verify`);

// ================= REVIEWS =================
export const getReviews = () =>
  api.get("/admin/reviews");

export const deleteReview = (reviewId) =>
  api.delete(`/admin/reviews/${reviewId}`);
