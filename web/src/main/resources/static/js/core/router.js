import _ from '../utils/utils.js'

export default class Router {
  constructor () {
    this.routes = {}
    window.onhashchange = this._processHashChange.bind(this)
  }

  // set routes(v) {}
  // set appRoot(v) {}

  _processHashChange (e) {
    const toRoute = e.newURL.split('#')[1] || '';
    const fromRoute = e.oldURL.split('#')[1] || '';
    if (window.location.hash.length > 0) {
      this._processRoute(fromRoute, _.clearSlashes(toRoute))
    } else /*if (this.config.routes['default'])*/ {
      this._processRoute(fromRoute, 'default')
    }
  }

  _processRoute (fromRoute, toRoute) {
    let activeRoute = 'error'
    let activeParams = {from: fromRoute, to: toRoute }
    for (let routePattern in this.routes) {
      const params = extractRouteParams(routePattern, toRoute)
      if(params) {
        params.prev  = fromRoute
        activeRoute  = routePattern
        activeParams = params
        break;
      }
    }

    this.routes[activeRoute](activeParams)

    if (this.afterHook) {
      this.afterHook(activeRoute, activeParams)
    }
  }

  navigate (path) {
    this.currentPath = path ? path : '';
    window.location.href = `${window.location.href.split('#')[0]}#${path}`;
    return this;
  }

  run () {
    this._processHashChange({ newURL: window.location.hash, oldURL: '#' })
    return this;
  }

  addRoute (routePattern, handler) {
    if (typeof routePattern === 'string') {
      this.routes[routePattern] = handler
    } else if (typeof routePattern === 'function') {
      this.routes['default'] = routePattern
    }
    return this;
  }

  afterProcessRoute (afterHook) {
    if (typeof afterHook === 'function') {
      this.afterHook = afterHook.bind(this)
    }
    return this;
  }

  delRoute (routePattern) {
    delete this.routes[routePattern]
    return this;
  }
}


const extractRouteParams = (routePattern, route) => {
  const [routeVariableNames, re] = toRouteRegExpr(routePattern)
  const matches = route.match(re)
  return matches ? matches.slice(1)
    .reduce((res, value, index) => {
      res[routeVariableNames[index]] = value
      return res
    }, {})
    : matches
}


const toRouteRegExpr = (pattern) => {
  let variableNames = [];

  const route = pattern.replace(/([:*])(\w+)/g, (full, dots, name) => {
    variableNames.push(name)
    return '([^\/]+)'
  }) + '/?$'

  return [variableNames, new RegExp(route)]
}