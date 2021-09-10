import { NativeModules } from 'react-native';

type PhoneSelectorType = {
  getPhoneNumber(): Promise<string>;
};

const { PhoneSelector } = NativeModules;

export default PhoneSelector as PhoneSelectorType;
