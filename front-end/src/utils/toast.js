import { toast } from "react-toastify";

export const successToast = (msg) =>
  toast.success(msg || "Success");

export const errorToast = (msg) =>
  toast.error(msg || "Something went wrong");

export const infoToast = (msg) =>
  toast.info(msg);

export const warningToast = (msg) =>
  toast.warn(msg);
