export default function SidebarItem({ label, active, onClick }) {
  return (
    <div
      onClick={onClick}
      className={`
        px-4 py-2 rounded-lg cursor-pointer transition
        ${active
          ? "bg-green-700 text-white"
          : "text-green-200 hover:bg-green-800"}
      `}
    >
      {label}
    </div>
  );
}
