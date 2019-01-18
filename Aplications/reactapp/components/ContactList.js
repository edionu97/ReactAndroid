import React, { Component } from 'react';

import {
  StyleSheet,
  FlatList,
  AsyncStorage,
  Text,
  ToastAndroid,
  ImageBackground,
  NetInfo
} from 'react-native';
import ContactService from '../services/Contacts';

import {
  Card,
  CardTitle,
  CardContent,
  CardAction,
  CardButton,
  CardImage
} from 'react-native-material-cards';
import CustomItem from './CustomItem';

import AddContact from './AddContacts';
import SyncService from '../services/Sync';

export default class ContactList extends Component {
  constructor() {
    super();
    this.state = {
      username: '',
      jwt: '',
      isModalVisible: false,
      contacts: []
    };

    this.removeContact = this.removeContact.bind(this);
    this.update = this.update.bind(this);
    this.connectionChanged = this.connectionChanged.bind(this);
  }

  async componentDidMount() {
    const token = await AsyncStorage.getItem('jwt');
    const user = await AsyncStorage.getItem('username');
    const connected = await NetInfo.isConnected.fetch();

    this.setState(prev => ({
      username: user,
      jwt: token
    }));

    ContactService.setOfflineInstance();
    if (connected) {
      ContactService.setOnlineInstance();
    }

    this.getContacts();

    NetInfo.addEventListener('connectionChange', this.connectionChanged);

    SyncService.getInstance().startSync(() => this.update());
  }

  connectionChanged(connectionInfo) {
    if (connectionInfo.type === 'none') {
      ToastAndroid.showWithGravity(
        'Offline',
        ToastAndroid.LONG,
        ToastAndroid.TOP
      );
      ContactService.setOfflineInstance();
      this.update();
      return;
    }

    ContactService.setOnlineInstance();
    this.update();
    ToastAndroid.showWithGravity('Online', ToastAndroid.LONG, ToastAndroid.TOP);
  }

  getContacts() {
    that = this;
    ContactService.getInstance()
      .getAllContacts(this.state.jwt)
      .then(resp => {
        that.setState(prev => ({
          contacts: resp.contacts
        }));
      })
      .catch(err => {
        console.log(err);
      });
  }

  addContact() {
    this.props.nav.navigate('AddScreen', { update: this.update });
  }

  removeContact(item) {
    ContactService.getInstance()
      .removeContact(this.state.jwt, item.firstName)
      .then(resp => {
        const message = resp.ok
          ? 'Contact removed successfully'
          : 'Could not remove contact!';

        if (resp.ok) {
          this.getContacts();
        }

        ToastAndroid.showWithGravity(
          message,
          ToastAndroid.LONG,
          ToastAndroid.TOP
        );
      })
      .catch(err => {
        ToastAndroid.showWithGravity(
          err.message,
          ToastAndroid.LONG,
          ToastAndroid.TOP
        );
      });
  }

  updateContact(item) {
    this.props.nav.navigate('UpdateScreen', {
      data: item,
      update: this.update
    });
  }

  update() {
    this.getContacts();
  }

  async componentWillUnmount() {
    await NetInfo.removeEventListener(
      'connectionChange',
      this.connectionChanged
    );

    await SyncService.getInstance().stopSync();
  }

  render() {
    // if we have no elments in list, than the default window is add window
    if (this.state.contacts.length === 0) {
      return (
        <ImageBackground
          source={require('../images/back.jpg')}
          style={styles.container}
        >
          <AddContact navigation={this.props.nav} update={this.update} />
        </ImageBackground>
      );
    }

    return (
      <ImageBackground
        source={require('../images/back.jpg')}
        style={styles.container}
      >
        <FlatList
          data={this.state.contacts}
          style={styles.list}
          renderItem={({ item }) => (
            <CustomItem>
              <Card style={styles.card}>
                <CardImage source={require('../images/back.jpg')} />
                <CardTitle />
                <CardContent avatarSource={require('../images/user.png')}>
                  <Text style={styles.contactInput}>
                    First Name: {item.firstName}
                  </Text>
                  <Text style={styles.contactInput}>
                    Last Name: {item.lastName}
                  </Text>
                  <Text style={styles.contactInput}>
                    Phone Number: {item.phoneNumber}
                  </Text>
                </CardContent>
                <CardAction separator={true} inColumn={false}>
                  <CardButton
                    onPress={() => {
                      this.updateContact(item);
                    }}
                    title="Update"
                    color="blue"
                  />
                  <CardButton
                    onPress={() => {
                      this.addContact();
                    }}
                    title="Add"
                    color="green"
                  />
                  <CardButton
                    onPress={() => {
                      this.removeContact(item);
                    }}
                    title="Delete"
                    color="red"
                  />
                </CardAction>
              </Card>
            </CustomItem>
          )}
          keyExtractor={(item, index) => index.toString()}
        />
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
  contactInput: {
    color: 'black'
  },
  list: {
    marginTop: 10,
    marginLeft: 5,
    marginRight: 5,
    marginBottom: 10
  },
  card: {
    borderTopWidth: 1,
    borderLeftWidth: 1,
    borderRightWidth: 1,
    borderColor: 'white',
    elevation: 1
  }
});
