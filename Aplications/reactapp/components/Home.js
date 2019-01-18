import React, { Component } from 'react';
import {
  StyleSheet,
  ImageBackground,
  TouchableOpacity,
  AsyncStorage,
  ToastAndroid,
  View,
  NetInfo
} from 'react-native';
import ContactList from './ContactList';
import AuthenticationService from '../services/Authentication';
import SyncService from '../services/Sync';
import ContactService from '../services/Contacts';

export default class Home extends Component {
  static navigationOptions = ({ navigation }) => {
    const { params = {} } = navigation.state;

    return {
      headerTitle: 'Contacts agenda',
      headerLeft: null,
      headerRight: (
        <View style={styles.bar}>
          <TouchableOpacity
            style={styles.buttonLogout}
            onPress={() => {
              params.onChartPress();
            }}
          >
            <ImageBackground
              source={require('../images/chart.png')}
              style={styles.logoutImage}
            />
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.buttonLogout}
            onPress={() => params.onLogout()}
          >
            <ImageBackground
              source={require('../images/logout.png')}
              style={styles.logoutImage}
            />
          </TouchableOpacity>
        </View>
      )
    };
  };

  onLogout(that) {
    this.auth
      .logout(this.state.jwt, this.state.username)
      .then(res => {
        AsyncStorage.removeItem('username').then(res => {
          AsyncStorage.removeItem('jwt');
          that.props.navigation.navigate('LoginScreen');
        });
      })
      .catch(err => {
        ToastAndroid.showWithGravity(
          err.message,
          ToastAndroid.LONG,
          ToastAndroid.BOTTOM
        );
      });
  }

  constructor() {
    super();
    this.state = {
      jwt: '',
      username: ''
    };
  }

  async componentDidMount() {
    const { navigation } = this.props;
    navigation.setParams({
      onLogout: () => this.onLogout(this),
      onChartPress: () => this.onChartPress()
    });

    const token = await AsyncStorage.getItem('jwt');
    const user = await AsyncStorage.getItem('username');

    this.setState(prev => ({
      username: user,
      jwt: token
    }));

    this.auth = AuthenticationService.getInstance();
  }

  buttonLoginPressed() {
    this.props.navigation.navigate('HomeScreen');
  }

  buttonRegisterPressed() {
    this.props.navigation.navigate('RegisterScreen');
  }

  onChartPress() {
    this.props.navigation.navigate('StatisticsScreeen');
  }

  render() {
    return (
      <ImageBackground
        source={require('../images/back1.jpg')}
        style={styles.container}
      >
        <ContactList nav={this.props.navigation} />
      </ImageBackground>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignSelf: 'stretch',
    backgroundColor: 'transparent',
    justifyContent: 'space-between'
  },
  buttonLogout: {},
  logoutImage: {
    width: 25,
    height: 25,
    marginRight: 15
  },
  formContainer: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'transparent'
  },
  imageContainer: {
    flex: 0,
    justifyContent: 'flex-end',
    paddingTop: 70,
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  bar: {
    flex: 1,
    flexDirection: 'row'
  },
  buttonContainers: {
    flex: 1,
    justifyContent: 'center'
  },
  image: {
    width: 100,
    height: 100
  },
  labelInput: {
    color: 'white'
  },
  formInput: {
    borderBottomWidth: 1,
    marginLeft: 70,
    marginRight: 70,
    marginTop: 40,
    borderColor: 'white'
  },
  input: {
    borderWidth: 0,
    color: 'white'
  },
  inputButton: {
    borderWidth: 0,
    color: 'white',
    fontWeight: 'bold'
  },
  inputAccount: {
    marginTop: 25,
    borderWidth: 0,
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 2
  },
  buttonHolder: {
    marginLeft: 60,
    marginRight: 60
  },
  buttonLogin: {
    flex: 1,
    borderRadius: 25,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25,
    backgroundColor: '#f0932b'
  },
  buttonRegister: {
    flex: 1,
    borderRadius: 25,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 25,
    backgroundColor: '#95afc0'
  }
});
