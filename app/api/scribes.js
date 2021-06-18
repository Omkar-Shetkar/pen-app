import apiClient from "./client";

const endPoint = "/scribes";

const find = (searchCriteria, language) => {
  console.log("*******************************************");
  console.log("Exam Date: ", searchCriteria.examDate);
  console.log("Exam Time: ", searchCriteria.examTime);
  console.log("*******************************************");

  const from = new Date(searchCriteria.examDate);

  console.log("search criteria: ", searchCriteria);
  const examTime = new Date(searchCriteria.examDate);
  const to = new Date(searchCriteria.examDate);
  to.setHours(to.getHours() + searchCriteria.durationHours);
  to.setMinutes(to.getMinutes() + searchCriteria.durationMinutes);

  // const fromDate = new Date(Date.UTC(2021, 5, 25, 6, 0, 0));
  // const toDate = new Date(Date.UTC(2021, 5, 25, 8, 0, 0));

  return apiClient.get(endPoint, {
    from: from.toISOString(),
    to: to.toISOString(),
    language: searchCriteria.language,
  });
};

export default {
  find,
};
