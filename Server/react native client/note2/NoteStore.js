import { httpApiUrl, wsApiUrl } from '../core/api';
import React, {Component} from 'react';
import {Provider} from './context';
import {getLogger} from "../core/utils";

const log = getLogger('NoteStore');

class NoteStore extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      notes: null,
      issue: null,
    };
  }

  componentDidMount() {
    log('componentDidMount');
    this.loadNotes();
    this.connectWs();
  }

  componentWillUnmount() {
    log('componentWillUnmount');
    this.disconnectWs();
  }

  loadNotes = () => {
    this.setState({ isLoading: false, issue: null });
    fetch(`${httpApiUrl}/note`)
      .then(response => response.json())
      .then(json => this.setState({ isLoading: false, notes: json.notes }))
      .catch(error => this.setState({ isLoading: false, issue: error }));
  };

  connectWs = () => {
    const ws = new WebSocket(wsApiUrl);
    ws.onopen = () => {};
    ws.onmessage = e => this.setState({
      notes: [JSON.parse(e.data).note].concat(this.state.notes || [])
    });
    ws.onerror = e => {};
    ws.onclose = e => {};
    this.ws = ws;
  };

  disconnectWs = () => {
    this.ws.close();
  };

  render() {
    return (
      <Provider value={this.state}>
        {this.props.children}
      </Provider>
    );
  }
}

export default NoteStore;