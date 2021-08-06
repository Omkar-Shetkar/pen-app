import apiClient from "./client";

const endPoint = "/exams/events";

const findExamEvents = (examEventSearchFilter) => {
  console.log("Find Exam Events API called...", examEventSearchFilter);
  return apiClient.get(endPoint, examEventSearchFilter);
};

export default {
  findExamEvents,
};
