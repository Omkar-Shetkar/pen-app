import Constants from "expo-constants";

const settings = {
  dev: {
    apiUrl: "http://192.168.1.5:8080/rest",
  },
  staging: {
    apiUrl: "http://192.168.1.5:8080/rest",
  },
  prod: {
    apiUrl: "http://192.168.1.5:8080/rest",
  },
};

const getCurrentSettings = () => {
  if (__DEV__) return settings.dev;
  if (Constants.manifest.releaseChannel === "staging") return settings.staging;
  return settings.prod;
};

export default getCurrentSettings();
