const testRuns = {
  34: {
    2: [
      { id: 1, name: 'Test 1', date: new Date(), rps: 10 },
      { id: 2, name: 'Test 2', date: new Date(), rps: 150 },
      { id: 3, name: 'Test 3', date: new Date(), rps: 150 },
      { id: 4, name: 'Test 4', date: new Date(), rps: 60 },
    ]
  },
  2: {
    1: [
      { id: 1, name: 'Test 1', date: new Date(), rps: 10 },
      { id: 2, name: 'Test 2', date: new Date(), rps: 150 },
      { id: 3, name: 'Test 4', date: new Date(), rps: 60 },
    ]
  },
  3: {
    2: [
      { id: 1, name: 'Test 1', date: new Date(), rps: 10 },
      { id: 2, name: 'Test 2', date: new Date(), rps: 150 },
      { id: 3, name: 'Test 4', date: new Date(), rps: 60 },
    ]
  }
}

const items = [
  {
    id: 1,
    name: 'Линия 1',
    projects: [
      { id: 1, name: 'Project 1' },
      { id: 2, name: 'Project 2' },
      { id: 3, name: 'Project 3' }
    ]
  },
  {
    id: 2,
    name: 'Business Line 2',
    projects: [
      { id: 1, name: 'Project 1' },
      { id: 2, name: 'Project 2' },
      { id: 3, name: 'Project 3' },
      { id: 4, name: 'Project 4' }
    ]
  },
  {
    id: 3,
    name: 'Super Line',
    projects: [
      { id: 1, name: 'Super project' },
      { id: 2, name: 'Giga project' },
      { id: 3, name: 'MF Project' }
    ]
  },

  {
    id: 34,
    name: 'Test',
    projects: [
      { id: 1, name: 'TestPr' },
      { id: 2, name: 'TestPr+' }
    ]
  }

]



export default class Model{
  constructor(data = {lines: items, tests: testRuns}, baseurl = ''){
    this.data = data
    this.apiUrl = baseurl 
  }
  projects(blId){
    const bl = this.businessLine(blId)
    return bl && bl.projects ? bl.projects : []
  }
  project(blId, projectId){
    return this.projects(blId).find(p => p.id === projectId)
  }
  get businessLines(){
    if(this.data) return this.data.lines;
    else return []
  }
  businessLine(id){
    return this.businessLines.find(bl => bl.id === id)
  }

  projectTests(blId, projectId) {
    if(this.project(blId, projectId) && this.data.tests){
      return this.data.tests[blId] && this.data.tests[blId][projectId] ? this.data.tests[blId][projectId] : []
    }
    else return []
  }
}




