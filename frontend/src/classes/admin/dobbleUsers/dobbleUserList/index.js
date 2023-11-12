import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
// import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class DobbleUserList extends Component {

    constructor(props) {
        super(props);
        this.state = { dobbleUsers: [] };
        this.remove = this.remove.bind(this);
        this.jwt = JSON.parse(window.localStorage.getItem("jwt"));
    }

    componentDidMount() {
        fetch('/api/v1/dobbleUsers', {
            headers: {
                "Authorization": `Bearer ${this.jwt}`,
            },
        })
            .then(response => response.json())
            .then(data => this.setState({ dobbleUsers: data }));
    }

    async remove(id) {
        await fetch(`/api/v1/dobbleUsers/${id}`, {
            method: 'DELETE',
            headers: {
                "Authorization": `Bearer ${this.jwt}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedVets = [...this.state.dobbleUsers].filter(i => i.id !== id);
            this.setState({ dobbleUsers: updatedDobbleUsers });
        });
    }

    render() {
        const { dobbleUsers, isLoading } = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const dobbleUserList = dobbleUsers.map(dobbleUser => {

            return <tr key={dobbleUser.id}>
                <td >{dobbleUser.email}</td>
                <td >{dobbleUser.user.username}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/dobbleUsers/" + dobbleUser.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(dobbleUser.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container style={{ marginTop: "15px" }} fluid>
                    <h1 className='text-center'>DobbleUsers</h1>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/dobbleUsers/new">Add DobbleUser</Button>{" "}
                    </div>
                    <Table className="mt-4">
                        <thead>
                            <tr>
                                <th width="20%">Email</th>
                                <th width="20%">Username</th>
                           
                                <th width="20%">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {dobbleUserList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }

}

export default DobbleUserList;

