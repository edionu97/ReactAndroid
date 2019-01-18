/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, { Component } from 'react';
import { StyleSheet, Easing, Animated } from 'react-native';
import Login from './components/Login';
import Register from './components/Register';
import { createStackNavigator, createAppContainer } from 'react-navigation';
import Home from './components/Home';
import AddContact from './components/AddContacts';
import UpdateContact from './components/UpdateContacts';
import PieChart from './components/Chart';

const transitionConfig = () => {
  return {
    transitionSpec: {
      duration: 800,
      easing: Easing.in(Easing.poly(4)),
      timing: Animated.timing,
      useNativeDriver: true
    },
    screenInterpolator: sceneProps => {
      const { layout, position, scene } = sceneProps;

      const thisSceneIndex = scene.index;
      const width = layout.initWidth;

      const translateX = position.interpolate({
        inputRange: [thisSceneIndex - 1, thisSceneIndex, thisSceneIndex + 1],
        outputRange: [width, 0, 1]
      });

      return { transform: [{ translateX }] };
    }
  };
};

const Navigator = createStackNavigator(
  {
    LoginScreen: {
      screen: Login
    },
    RegisterScreen: {
      screen: Register
    },
    HomeScreen: {
      screen: Home
    },
    AddScreen: {
      screen: AddContact
    },
    UpdateScreen: {
      screen: UpdateContact
    },
    StatisticsScreeen: {
      screen: PieChart
    }
  },
  {
    initialRouteName: 'LoginScreen',
    transitionConfig
  }
);

const Nav = createAppContainer(Navigator);

export default class App extends Component {
  constructor() {
    super();
  }

  render() {
    return <Nav />;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100%'
  }
});
