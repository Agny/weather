import React, { Component } from 'react';
import moment from 'moment';
import CityWithDates from './CityWithDates';
import 'react-datepicker/dist/react-datepicker.css';

class DataForm extends Component {

  constructor(props) {
    super(props);
    this.state = {
      apiKey: '',
      base: {
        city: '',
        startDate: moment(),
        endDate: moment()
      },
      compareWith: {
        city: '',
        startDate: moment(),
        endDate: moment()
      },
      periodType: this.props.periodType
    };

    this.changeApi = this.changeApi.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  changeApi(event) {
    this.setState({apiKey: event.target.value});
  }

  changeState(data) {
    this.setState(data);
  }

  componentWillReceiveProps(parentProps) {
    this.setState({
      periodType: parentProps.periodType
    })
  }

  handleSubmit(event) {
    const bsDate = this.state.base.startDate.format("YYYY-MM-DD");
    const beDate = this.state.base.endDate.format("YYYY-MM-DD");
    const csDate = this.state.compareWith.startDate.format("YYYY-MM-DD");
    const ceDate = this.state.compareWith.endDate.format("YYYY-MM-DD");
    const body = {
      apiKey: this.state.apiKey,
      base: {
        city: this.state.base.city,
        startDate: bsDate,
        endDate: beDate
      },
      compareWith: {
        city: this.state.compareWith.city,
        startDate: csDate,
        endDate: ceDate
      },
      periodType: this.state.periodType
    };
    this.props.loadData(body);
    event.preventDefault();
  }

  render() {
    return (
        <form onSubmit={this.handleSubmit}>
          <div>
            <div style={{float:"right"}}>
              <label>
                Api key
                <input type="text" value={this.state.apiKey} onChange={this.changeApi}/>
              </label>
            </div>
            <CityWithDates changeState={this.changeState.bind(this)} fields={this.state.base} name="base"/>
            {
              this.props.isComparing
                  ? <CityWithDates changeState={this.changeState.bind(this)} fields={this.state.compareWith}
                                   name="compareWith"/>
                  : null

            }
            <input type="submit" value="Load"/>
          </div>
        </form>
    );
  }
}

export default DataForm;