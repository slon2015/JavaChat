import React, { Component } from 'react';
//import 'bootstrap/dist/css/bootstrap.css';
import '../main.min.css'
import Contact from './Contact'
import {Grid, Row, Col, Button} from 'react-bootstrap'


class App extends Component {

  constructor(props, context) {
      super(props, context);

      this.state = {
          open: false,
          contacts: this.props.contacts,
          currentChat: null
      };
  }

  toggleCollapse(){
    this.setState({
        open: !this.state.open
    });
  }

  switchContact(contact){
    this.setState({
        currentChat: contact.chat
    });
  }

  renderChat(){
    if(this.state.currentChat != null) {
        return (
            this.state.currentChat.map( (value, index) => {
              return ([<span key={index}>{value}</span>, <br/>])
            })
        )
    }
    else{
      return (
          <span>История сообщений...</span>
      );
    }
  }

  render() {
    return (
    <div>
      <div className="wrapper d-flex align-items-stretch">
        {/* SIDEBAR */}
          <nav id="sidebar" style={{marginLeft: this.state.open? 0:-300}}>
            <div className="sidebar-header">
              <h3>Контакты</h3>
            </div>
            <div className="contacts">
              <ul className="list-group">
                {
                  this.state.contacts.map( (value, index) => {
                    return (<Contact key={index} name={value.name}
                                     messagesCount={value.messageCount}
                                     onClick={this.switchContact.bind(this, value)}/>);
                  })
                }
              </ul>
            </div>
          </nav>
        {/* MESSAGES. MAIN CONTENT */}
        <section id="content">
          <Grid fluid={true}>
            <Row className="chat">
              <Col md={12} className="form d-flex flex-column justify-content-between p-0">
                <div className="form_header">
                  <Button bsStyle="primary" onClick={this.toggleCollapse.bind(this)}>
                    <i className="fas fa-bars" />
                  </Button>
                </div>
                {/* ИСТОРИЯ СООБЩЕНИЙ */}
                <div className="messages text-center">
                    {this.renderChat()}
                </div>
                <div className="input-group">
                  <input className="form-control" placeholder="Напишите сообщение..." type="text" />
                  <span className="input-group-append">
                    <Button bsStyle="primary">
                      <i className="far fa-check-circle" />
                    </Button>
                  </span>
                </div>
              </Col>
            </Row>
          </Grid>
        </section>
      </div>
    </div>);
  }
}

export default App;
