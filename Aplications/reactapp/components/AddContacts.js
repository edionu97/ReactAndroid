import React, { Component } from 'react';
import {
  StyleSheet,
  Text,
  View,
  Image,
  TouchableOpacity,
  ImageBackground,
  ToastAndroid,
  AsyncStorage
} from 'react-native';

import FloatingLabel from 'react-native-floating-labels';
import ContactValidator from '../validators/ContactValidator';
import ContactService from '../services/Contacts';

export default class AddContact extends Component {
  static navigationOptions = {
    header: null
  };

  constructor() {
    super();

    this.buttonAddPressed = this.buttonAddPressed.bind(this);
    this.buttonCancelPressed = this.buttonCancelPressed.bind(this);
    this.state = {
      first: '',
      last: '',
      jwt: '',
      phone: ''
    };
  }

  buttonCancelPressed() {
    this.props.navigation.navigate('HomeScreen');
  }

  async componentDidMount() {
    token = await AsyncStorage.getItem('jwt');
    this.setState({
      jwt: token
    });
  }

  async buttonAddPressed() {
    try {
      ContactValidator.validate(
        this.state.first,
        this.state.last,
        this.state.phone
      );
      ContactService.getInstance()
        .addContact(this.state.jwt, {
          first: this.state.first,
          last: this.state.last,
          phone: this.state.phone
        })
        .then(resp => {
          const message = resp.ok
            ? 'Contact inserted successfully'
            : 'Duplicates are not allowed';

          ToastAndroid.showWithGravity(
            message,
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );

          if (resp.ok) {
            const updateMethod = this.props.navigation.state.params.update
              ? this.props.navigation.state.params.update
              : this.props.update;

            updateMethod();
          }
        })
        .catch(error => {
          ToastAndroid.showWithGravity(
            error.message,
            ToastAndroid.LONG,
            ToastAndroid.TOP
          );
        });
    } catch (error) {
      ToastAndroid.showWithGravity(
        error.message,
        ToastAndroid.LONG,
        ToastAndroid.TOP
      );
    }
  }

  render() {
    return (
      <ImageBackground
        source={require('../images/back1.jpg')}
        style={styles.container}
      >
        <View style={styles.imageContainer}>
          <View>
            <Image style={styles.image} source={require('../images/add.png')} />
          </View>
          <View>
            <Text
              style={{
                color: 'white',
                fontSize: 20,
                paddingTop: 15
              }}
            >
              Add new contact
            </Text>
          </View>
        </View>
        <View style={styles.formContainer}>
          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ first: value })}
          >
            Enter first name
          </FloatingLabel>

          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ last: value })}
          >
            Enter last name
          </FloatingLabel>
          <FloatingLabel
            labelStyle={styles.labelInput}
            inputStyle={styles.input}
            style={styles.formInput}
            onChangeText={value => this.setState({ phone: value })}
          >
            Enter phone number
          </FloatingLabel>
        </View>

        <View style={styles.buttonContainers}>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonLogin}
              onPress={this.buttonAddPressed}
            >
              <Text style={styles.inputButton}>ADD</Text>
            </TouchableOpacity>
          </View>
          <View>
            <Text style={styles.inputAccount}>Have you changed your mind?</Text>
          </View>
          <View style={styles.buttonHolder}>
            <TouchableOpacity
              style={styles.buttonRegister}
              onPress={this.buttonCancelPressed}
            >
              <Text style={styles.inputButton}>CANCEL </Text>
            </TouchableOpacity>
          </View>
        </View>
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
  formContainer: {
    flex: 1,
    justifyContent: 'center',
    backgroundColor: 'transparent'
  },
  imageContainer: {
    flex: 0,
    justifyContent: 'flex-end',
    paddingTop: 20,
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  buttonContainers: {
    flex: 1,
    justifyContent: 'center'
  },
  image: {
    width: 90,
    height: 90
  },
  labelInput: {
    color: 'white'
  },
  formInput: {
    borderBottomWidth: 1,
    marginLeft: 70,
    marginRight: 70,
    marginTop: 25,
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
