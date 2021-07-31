const strings = {
  account: {
    a11y: {
      accountButton: "Tap here to know your account information",
    },
    success: "Updated successfully!",
    failed: "Updated failed!",
  },
  candidate: {
    a11y: {
      applied: "Following requests are pending",
      accepted: "Following requests are accepted",
      archived: "Following are previous completed requests",
    },
  },
  common: {
    serverError: "Snap, error with server!\n Please try after sometime.",
  },
  login: {
    a11y: {
      mobile: "Please enter your mobile number",
      password: "Please enter your password",
      login: "Press to login",
    },
    sessionExpired: "Your session has expired! \n Please login again.",
  },
  register: {
    scribe: "Scribe writes exams for others",
    candidate: "Candidate gets assistance for his exams",
  },
  request: {
    a11y: {
      requestButton:
        "Tap here to know status of requests which are applied, accepted or archived.",
      appliedButton:
        "Tap to know requests which are applied but not yet accepted",
      acceptedButton: "Tap to know requests which are accepted",
      archiveButton: "Tap to know about past requests",
      message: "You have selected information card for ",
      exam: "For the exam ",
      language: " and language as ",
      start: "Starts at, ",
      end: "Ends at, ",
      sendRequest: "Select to send request to ",
    },
    sent: "Request sent successfully to ",
    cancel: "Cancelled the request",
    failed: "Request update failed!",
    accept: "Accepted the request",
    decline: "Declined the request",
    noData: "No Data Found!",
  },
  search: {
    a11y: {
      searchButton: "Tap here to search for scribes",
      examDateTime: "Tap to select exam date and time",
      examDurationHr: "Select exam duration hours and minutes",
      examDurationMin: "Select exam duration minutes",
      examLanguage: "Select exam language",
      examName: "Select exam name",
      examAddress: "Enter exam location address",
      examCity: "Select exam location city",
      result: " is available to write exam",
    },
    examError: "Exam is a required field",
    languageError: "Language is a required field",
    cityError: "City is a required field",
    noScribeFound: "No scribes available!\nPlease try after sometime.",
  },
  welcome: {
    a11y: {
      welcomeMessage:
        "Welcome to 'Pen' App. If you are new, please tap 'Register'. Otherwise, please tap 'Login' to continue.",
      login: "Opens login screen",
      register: "Opens register screen",
    },
    slogan: "Connecting Scribe with Aspirants",
  },
};

export default strings;
