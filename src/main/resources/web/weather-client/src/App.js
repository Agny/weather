import React, { Component } from 'react';
import moment from 'moment';
import DataChart from './DataChart';
import DataForm from './DataForm';

const properties = require('./settings.json');

const millisInDay = 24 * 60 * 60 * 1000;
const apiHost = properties.host;
const apiPort = properties.port;

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      isComparing: false,
      rawData: null,
      processedData: null,
      processedDataToCompare: null,
      periodType: 'all'
    };

    this.loadData = this.loadData.bind(this);
  }

  changeComparing(data) {
    this.setState({isComparing: data});
  }

  changePeriod(data) {
    this.setState({periodType: data});
  }

  loadData(params) {
    const _this = this;
    this.loadRawData(params, _this);
    this.loadProcessedData(params, _this);
  }

  loadRawData(params, that) {
    fetch('http://' + apiHost + ':' + apiPort + '/api/raw', {
      method: "POST",
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Headers': 'Content-Type',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(params)
    }).then(function (resp) {
      return resp.json()
    }).then(function (data) {
      if (data.data) {
        const res = that.handleRawData(data, that);
        that.setState({rawData: res});
      }
    }).catch(function () {
      console.log("what to do what to do");
    });
  }

  loadProcessedData(params, that) {
    fetch('http://' + apiHost + ':' + apiPort + '/api/processed', {
      method: "POST",
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Headers': 'Content-Type',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(params)
    }).then(function (resp) {
      return resp.json()
    }).then(function (data) {
      if (data.left && data.right) {
        const left = that.handleProcessedData(data.left);
        const right = that.handleProcessedData(data.right);
        that.setState({processedData: left});
        that.setState({processedDataToCompare: right});
      } else {
        const left = that.handleProcessedData(data);
        that.setState({processedData: left});
      }
    }).catch(function () {
      const er = {error: "Data is empty. Please, choose other time range: specified dates must be in past to correctly retrieve historical data"};
      console.log(er);
    });
  }

  handleRawData(response) {
    const result = [];
    const data = response.data;
    data.forEach(function (item, i, arr) {
      const day = moment.utc(item.date * millisInDay).format("YYYY-MM-DD");
      item.hours.forEach(function (hour, j, arr2) {
        result.push({
          date: day + ' ' + hour.time,
          temperature: hour.temperature,
          humidity: hour.humidity,
          windSpeed: hour.wind.speed,
          windDirection: hour.wind.direction
        })
      })
    });
    return result;
  }

  handleProcessedData(response) {
    const result = {};
    result.temperature = App.getStatData(response.temperature);
    result.humidity = App.getStatData(response.humidity);
    result.windSpeed = App.getStatData(response.windSpeed);

    const data = [];
    const source = response.source;
    source.data.forEach(function (item, i, arr) {
      const day = moment.utc(item.date * millisInDay).format("YYYY-MM-DD");
      item.hours.forEach(function (hour, j, arr2) {
        data.push({
          date: day + ' ' + hour.time,
          temperature: hour.temperature,
          humidity: hour.humidity,
          windSpeed: hour.wind.speed,
          windDirection: hour.wind.direction
        })
      })
    });
    result.data = data;
    return result;
  }

  static getStatData(from) {
    return {
      d: from.stDeviation,
      m: from.median,
      min: from.min,
      max: from.max
    }
  }

  render() {
    return (
        <div>
          <div className="left">
            <div id="controls">
              <DataForm isComparing={this.state.isComparing}
                        periodType={this.state.periodType}
                        loadData={this.loadData}
              />
            </div>
          </div>
          <div className="right">
            <div id="dataContainer">
              <DataChart isComparing={this.state.isComparing}
                         changeComparing={this.changeComparing.bind(this)}
                         rawData={this.state.rawData}
                         processedData={this.state.processedData}
                         processedDataToCompare={this.state.processedDataToCompare}
                         periodType={this.state.periodType}
                         changePeriod={this.changePeriod.bind(this)}
              />
            </div>
          </div>
          <div className="clearBoth"></div>
        </div>
    )
  }
}

export default App;