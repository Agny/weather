import React, { Component } from 'react';
import {AxisTick, CustomTooltip, WindTooltip} from './CustomChartElements';
import './App.css';
import { AreaChart, Area, Brush, LineChart, ReferenceLine, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
import {Bar, BarChart} from 'recharts';

import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import {RadioGroup, Radio} from 'react-radio-group';
import 'react-tabs/style/react-tabs.css';

class CustomToggle extends Component {

  constructor(props) {
    super(props);

    this.handleChange = this.handleChange.bind(this);
  }

  handleChange() {
    this.props.changeComparing(!this.props.isComparing);
  }

  render() {
    const {name} = this.props;
    return (
        <div id="compareFlag">
          <input onChange={this.handleChange} id={this.id} type="checkbox" checked={this.props.isComparing}/>
          <label htmlFor={this.id}>{name}</label>
        </div>
    );
  }
}

class DataChart extends Component {

  constructor(props) {
    super(props);
    this.handleChangePeriod = this.handleChangePeriod.bind(this);
  }

  handleChangePeriod(value) {
    this.props.changePeriod(value);
  }

  render() {
    const {isComparing, changeComparing, rawData, processedData, processedDataToCompare} = this.props;
    return (
        <Tabs>
          <TabList>
            <Tab>Raw History Data</Tab>
            <Tab>Processed Data</Tab>
          </TabList>
          <TabPanel>
            {rawData
                ? <div>
              <LineChart width={800} height={280} data={rawData} syncId="selfId"
                         margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip content={<CustomTooltip/>}/>
                <Legend/>
                <Line name="Temperature, ℃" type='monotone' dataKey='temperature' stroke='#8884d8' fill='#8884d8'/>
              </LineChart>
              <AreaChart width={800} height={280} data={rawData} syncId="selfId"
                         margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip content={<CustomTooltip/>}/>
                <Area name="Humidity, %" type='monotone' dataKey='humidity' stroke='#82ca9d' fill='#82ca9d'/>
                <Legend/>
              </AreaChart>
              <BarChart width={800} height={280} data={rawData} syncId="selfId"
                        margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip content={<WindTooltip/>}/>
                <Legend />
                <Bar name="Wind speed, km/h" dataKey="windSpeed" fill="#8884d8"/>
                <Brush />
              </BarChart>
            </div>
                : <div></div>
            }
          </TabPanel>
          <TabPanel>
            <div>
              <RadioGroup name="periods" selectedValue={this.props.periodType} onChange={this.handleChangePeriod}>
                <Radio value="all"/>Whole period
                <Radio value="daytime"/>During daytime
                <Radio value="nighttime"/>During nighttime
              </RadioGroup>
            </div>
            <div style={{float:"left"}}>
              <CustomToggle isComparing={isComparing} changeComparing={changeComparing} name="Compare"/>
            </div>
            {processedData
                ? <div>
              <div className="left">
                <LineChart width={750} height={280} data={processedData.data}
                           margin={{top: 10, right: 30, left: 0, bottom: 0}}
                           syncId="pId"
                >
                  <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                  <YAxis/>
                  <CartesianGrid strokeDasharray="3 3"/>
                  <Tooltip/>
                  <Legend/>
                  <ReferenceLine y={processedData.temperature.m + processedData.temperature.d} label="Median + s"
                                 stroke="red" strokeDasharray="3 3"/>
                  <ReferenceLine y={processedData.temperature.m - processedData.temperature.d} label="Median - s"
                                 stroke="blue" strokeDasharray="3 3"/>
                  <ReferenceLine y={processedData.temperature.min} label="Min" stroke="blue"/>
                  <ReferenceLine y={processedData.temperature.max} label="Max" stroke="red"/>
                  <Line name="Temperature, °" type='monotone' dataKey='temperature' stroke='#8884d8'/>
                </LineChart>
                <AreaChart width={750} height={280} data={processedData.data}
                           margin={{top: 10, right: 30, left: 0, bottom: 0}}
                           syncId="pId"
                >
                  <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                  <YAxis/>
                  <CartesianGrid strokeDasharray="3 3"/>
                  <Tooltip/>
                  <ReferenceLine y={processedData.humidity.m + processedData.humidity.d} label="Median + s" stroke="red"
                                 strokeDasharray="3 3"/>
                  <ReferenceLine y={processedData.humidity.m - processedData.humidity.d} label="Median - s"
                                 stroke="blue" strokeDasharray="3 3"/>
                  <ReferenceLine y={processedData.humidity.min} label="Min" stroke="blue"/>
                  <ReferenceLine y={processedData.humidity.max} label="Max" stroke="red"/>
                  <Area name="Humidity, %" type='monotone' dataKey='humidity' stroke='#88cad8' fill='#88cad8'/>
                  <Legend/>
                </AreaChart>
                <BarChart width={750} height={300} data={processedData.data}
                          margin={{top: 10, right: 30, left: 0, bottom: 0}}
                          syncId="pId"
                >
                  <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                  <YAxis/>
                  <CartesianGrid strokeDasharray="3 3"/>
                  <Tooltip/>
                  <Legend />
                  <ReferenceLine isFront={true} y={processedData.windSpeed.m + processedData.windSpeed.d}
                                 label="Median + s" stroke="red" strokeDasharray="3 3"/>
                  <ReferenceLine isFront={true} y={processedData.windSpeed.m - processedData.windSpeed.d}
                                 label="Median - s" stroke="blue" strokeDasharray="3 3"/>
                  <ReferenceLine isFront={true} y={processedData.windSpeed.min} label="Min" stroke="blue"/>
                  <ReferenceLine isFront={true} y={processedData.windSpeed.max} label="Max" stroke="red"/>
                  <Bar name="Wind speed, km/h" dataKey="windSpeed" fill="#82ca9d"/>
                  <Brush />
                </BarChart>
              </div>
            </div>
                : <div></div>
            }
            {this.props.isComparing && processedDataToCompare
                ? <div style={{float:"right", position:"relative"}}>
              <LineChart width={750} height={280} data={processedDataToCompare.data}
                         margin={{top: 10, right: 30, left: 0, bottom: 0}}
                         syncId="pcId"
              >
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip/>
                <Legend/>
                <ReferenceLine y={processedDataToCompare.temperature.m + processedDataToCompare.temperature.d}
                               label="Median + s" stroke="red" strokeDasharray="3 3"/>
                <ReferenceLine y={processedDataToCompare.temperature.m - processedDataToCompare.temperature.d}
                               label="Median - s" stroke="blue" strokeDasharray="3 3"/>
                <ReferenceLine y={processedDataToCompare.temperature.min} label="Min" stroke="blue"/>
                <ReferenceLine y={processedDataToCompare.temperature.max} label="Max" stroke="red"/>
                <Line name="Temperature, °" type='monotone' dataKey='temperature' stroke='#8884d8'/>
              </LineChart>
              <AreaChart width={750} height={280} data={processedDataToCompare.data}
                         margin={{top: 10, right: 30, left: 0, bottom: 0}}
                         syncId="pcId"
              >
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip/>
                <ReferenceLine y={processedDataToCompare.humidity.m + processedDataToCompare.humidity.d}
                               label="Median + s" stroke="red" strokeDasharray="3 3"/>
                <ReferenceLine y={processedDataToCompare.humidity.m - processedDataToCompare.humidity.d}
                               label="Median - s" stroke="blue" strokeDasharray="3 3"/>
                <ReferenceLine y={processedDataToCompare.humidity.min} label="Min" stroke="blue"/>
                <ReferenceLine y={processedDataToCompare.humidity.max} label="Max" stroke="red"/>
                <Area name="Humidity, %" type='monotone' dataKey='humidity' stroke='#88cad8' fill='#88cad8'/>
                <Legend/>
              </AreaChart>
              <BarChart width={750} height={300} data={processedDataToCompare.data}
                        margin={{top: 10, right: 30, left: 0, bottom: 0}}
                        syncId="pcId"
              >
                <XAxis dataKey="date" height={95} tick={<AxisTick/>}/>
                <YAxis/>
                <CartesianGrid strokeDasharray="3 3"/>
                <Tooltip/>
                <Legend />
                <ReferenceLine isFront={true}
                               y={processedDataToCompare.windSpeed.m + processedDataToCompare.windSpeed.d}
                               label="Median + s" stroke="red" strokeDasharray="3 3"/>
                <ReferenceLine isFront={true}
                               y={processedDataToCompare.windSpeed.m - processedDataToCompare.windSpeed.d}
                               label="Median - s" stroke="blue" strokeDasharray="3 3"/>
                <ReferenceLine isFront={true} y={processedDataToCompare.windSpeed.min} label="Min" stroke="blue"/>
                <ReferenceLine isFront={true} y={processedDataToCompare.windSpeed.max} label="Max" stroke="red"/>
                <Bar name="Wind speed, km/h" dataKey="windSpeed" fill="#82ca9d"/>
                <Brush />
              </BarChart>
            </div>
                : <div></div>
            }
          </TabPanel>
        </Tabs >
    );
  }
}

export default DataChart;
