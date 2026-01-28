function InfoRow({ label, value }) {
  return (
    <div className="flex justify-between items-center border-b pb-2 last:border-b-0">
      <span className="text-gray-500 font-medium">
        {label}
      </span>
      <span className="text-gray-900 font-semibold">
        {value || "-"}
      </span>
    </div>
  );
}
export default InfoRow;