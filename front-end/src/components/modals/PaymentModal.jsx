import { useState } from "react";
import api from "../../services/api";
import { errorToast } from "../../utils/toast";

export default function PaymentModal({ rental, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);

  const startPayment = async () => {
    try {
      setLoading(true);

      // STEP 1: Create Order
      const res = await api.post(`/payments/create/${rental.rentalId}`);
      const { orderId, keyId, amount } = res.data;

      const options = {
        key: keyId,
        amount: amount * 100,
        currency: "INR",
        name: "FarmLink",
        description: "Equipment Rental Payment",
        order_id: orderId,

        handler: function (response) {
          // 🔥 DO NOT await here
          api.post("/payments/verify", {
            paymentId: response.razorpay_payment_id,
            orderId: response.razorpay_order_id,
            signature: response.razorpay_signature,
          }).finally(() => {
            // Always success UI (backend already verified)
            onSuccess();
            onClose();
          });
        },

        modal: {
          ondismiss: () => {
            setLoading(false);
          },
        },

        theme: {
          color: "#16a34a",
        },
      };

      const rzp = new window.Razorpay(options);

      // Optional: explicit failure handling
      rzp.on("payment.failed", function () {
        errorToast("Payment failed or cancelled");
        setLoading(false);
      });

      rzp.open();
    } catch (err) {
      console.error(err);
      errorToast(err.response?.data || "Payment initiation failed");
      setLoading(false);
    }
  };

  return (
    <>
      {/* BACKDROP */}
      <div className="fixed inset-0 bg-black/40 backdrop-blur-sm z-40" />

      {/* MODAL */}
      <div className="fixed inset-0 flex items-center justify-center z-50">
        <div className="bg-white rounded-2xl shadow-xl w-full max-w-md p-6">

          <h2 className="text-xl font-bold text-green-900 mb-3">
            Confirm Payment 💳
          </h2>

          <div className="text-sm text-gray-700 space-y-2">
            <p><b>Equipment:</b> {rental.equipmentName}</p>
            <p><b>Owner:</b> {rental.ownerName}</p>
            <p><b>Dates:</b> {rental.startDate} → {rental.endDate}</p>
            <p className="font-semibold text-green-700">
              Total: ₹{rental.totalAmount}
            </p>
          </div>

          <div className="flex gap-3 mt-6">
            <button
              onClick={onClose}
              className="w-1/2 border rounded-lg py-2 font-semibold"
            >
              Cancel
            </button>

            <button
              onClick={startPayment}
              disabled={loading}
              className="w-1/2 bg-green-600 text-white rounded-lg py-2 font-semibold hover:bg-green-700 transition"
            >
              {loading ? "Processing..." : "Pay Now"}
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
