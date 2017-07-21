import React, { Component } from 'react';
import moment from 'moment';
import DatePicker from 'react-datepicker';
class CityWithDates extends Component {
  constructor(props) {
    super(props);

    this.changeCity = this.changeCity.bind(this);
    this.changeStart = this.changeStart.bind(this);
    this.changeEnd = this.changeEnd.bind(this);
  }

  changeCity(event) {
    const update = {[this.props.name]: this.props.fields};
    update[[this.props.name]].city = event.target.value;
    this.props.changeState(update);
  }

  changeStart(date) {
    const dateString = date.format("YYYY-MM-DD");
    const update = {[this.props.name]: this.props.fields};
    update[[this.props.name]].startDate = moment.utc(dateString);
    this.props.changeState(update);
  }

  changeEnd(date) {
    const dateString = date.format("YYYY-MM-DD");
    const update = {[this.props.name]: this.props.fields};
    update[[this.props.name]].endDate = moment.utc(dateString);
    this.props.changeState(update);
  }

  render() {
    const {fields} = this.props;
    return (
        <div className="form-block">
          <div>
            <label>
              City name
              <input type="text" value={fields.city} onChange={this.changeCity}/>
            </label>
          </div>
          <div style={{float:"right"}}>
            <DatePicker
                selected={fields.startDate}
                selectsStart
                startDate={fields.startDate}
                endDate={fields.endDate}
                onChange={this.changeStart}
                peekNextMonth
                showMonthDropdown
                showYearDropdown
                dropdownMode="select"
            />
            <DatePicker
                selected={fields.endDate}
                selectsEnd
                startDate={fields.startDate}
                endDate={fields.endDate}
                onChange={this.changeEnd}
                peekNextMonth
                showMonthDropdown
                showYearDropdown
                dropdownMode="select"
            />
          </div>
        </div>
    );
  }
}

export default CityWithDates;