import apiClient from "./client";

const endPoint = "/scribes";

const find = (searchCriteria, language) => {
  const from = new Date(searchCriteria.examDate);

  console.log("search criteria: ", searchCriteria);

  const to = new Date(from);
  to.setHours(to.getHours() + parseInt(searchCriteria.durationHours));
  to.setMinutes(to.getMinutes() + parseInt(searchCriteria.durationMinutes));

  return apiClient.get(endPoint, {
    from: from.toISOString(),
    to: to.toISOString(),
    language: searchCriteria.language,
  });
};

export default {
  find,
};
