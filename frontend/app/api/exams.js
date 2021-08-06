import apiClient from "./client";

const endPoint = "/exams";
const getExams = () => apiClient.get(endPoint);

export default {
  getExams,
};
