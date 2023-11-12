import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { Button, Col, Container, Form, FormGroup, Input, Label, Row } from 'reactstrap';

class DobbleUserEdit extends Component {

    emptyDobbleUser = {
        id: '',
        email: '',
        username: '',
        authority: 5,
        user: { id: 1 },
    };

    constructor(props) {
        super(props);
        this.state = {
            dobbleUser: this.emptyDobbleUser,
            // users: [],
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSpecialtyChange = this.handleSpecialtyChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.jwt = JSON.parse(window.localStorage.getItem("jwt"));
        let pathArray = window.location.pathname.split('/');
        this.id = pathArray[2];
    }

    async componentDidMount() {
        if (this.id !== 'new') {
            const dobbleUser = await (await fetch(`/api/v1/dobbleUsers/${this.id}`, {
                headers: {
                    "Authorization": `Bearer ${this.jwt}`,
                },
            })).json();
            this.setState({ dobbleUser: dobbleUser });
        }



        const users = await (await fetch(`/api/v1/users?auth=PLAYER`, {
            headers: {
                "Authorization": `Bearer ${this.jwt}`,
            },
        })).json();
        this.setState({ users: users });
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let dobbleUser = { ...this.state.dobbleUser };
        if (name === "user") {
            dobbleUser.user.id = value;
        } else dobbleUser[name] = value;
        this.setState({ dobbleUser });
    }

    handleSpecialtyChange(event) {
        const target = event.target;
        const checked = target.checked;
        const name = target.name;
        let dobbleUser = { ...this.state.dobbleUser };
        this.setState({ dobbleUser });
    }

    async handleSubmit(event) {
        event.preventDefault();
        const { dobbleUser } = this.state;

        await fetch('/api/v1/dobbleUsers' + (dobbleUser.id ? '/' + dobbleUser.id : ''), {
            method: (dobbleUser.id) ? 'PUT' : 'POST',
            headers: {
                "Authorization": `Bearer ${this.jwt}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(dobbleUser),
        });
        window.location.href = '/dobbleUsers';
    }

    render() {
        const title = <h2>{dobbleUser.id ? 'Edit DobbleUser' : 'Add DobbleUser'}</h2>;

        // const userOptions = users.map(user => <option key={user.id} value={user.id}>{user.username}</option>);


        return (
            <div>
                <Container className="d-flex ">
                    <Col md={4}>
                        {title}
                        <Form onSubmit={this.handleSubmit}>
                            <FormGroup>
                                <Label for="email">Email</Label>
                                <Input type="text" name="email" id="email" value={dobbleUser.email || ''}
                                    onChange={this.handleChange} autoComplete="email" />
                            </FormGroup>
                            <FormGroup>
                                <Label for="username">Username</Label>
                                <Input type="text" name="username" id="username" value={dobbleUser.username || ''}
                                    onChange={this.handleChange} autoComplete="username" />
                            </FormGroup>
                            
                      
                            {dobbleUser.id ?
                                <FormGroup>
                                    <Label for="user">User</Label>
                                    <p>{dobbleUser.user.username || ''}</p>
                                </FormGroup> : <></>
                            }
                            <br></br>
                            <FormGroup>
                                <Button color="primary" type="submit">Save</Button>{' '}
                                <Button color="secondary" tag={Link} to="/dobbleUsers">Cancel</Button>
                            </FormGroup>
                        </Form>
                    </Col>
                </Container>
            </div >
        )
    }
}
export default DobbleUserEdit;