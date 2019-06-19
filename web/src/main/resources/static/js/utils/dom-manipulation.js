import {VNode} from './vdom.js'

class DOMUtils {
  constructor (val) {
    if (val === null || val === undefined) { this._value = []; return }
    if (val instanceof NodeList) {
      this._value = Array.from(val)
    } else if (val instanceof Array) {
      this._value = val
    } else {
      this._value = [val]
    }
  }

  qs (selector) {
    return this.flatMap(e =>
      selector && selector !== '' ? new DOMUtils(e.querySelector(selector)) : this
    )
  }

  qsAll (selector) {
    return this.flatMap(e =>
      selector && selector !== '' ? new DOMUtils(e.querySelectorAll(selector)) : this
    )
  }

  qId (id) {
    return this.flatMap(e =>
      selector && selector !== '' ? new DOMUtils(e.getElementById(id)) : this
    )
  }

  map (fn) {
    if (this._value.length > 0 && typeof fn === 'function') {
      this._value = this._value.map(fn)
    }
    this._value = this._value.filter(e => e !== undefined)

    return this;
  }

  forEach (fn) {
    if (this._value.length > 0 && typeof fn === 'function') {
      this._value.forEach(fn)
    }
    return this;
  }

  flatMap (fn) {
    if (this._value.length > 0 && typeof fn === 'function') {
      if (this._value.length == 1) {
        return this._value.map(fn)[0]
      }
      return this._value
        .map(fn)
        .reduce((acc, du) => {
          acc._value.push(du._value)
          return acc
        }, new DOMUtils()
        )
    } else {
      return this;
    }
  }

  forAll (pred) {
    if (this._value.length > 0 && typeof pred === 'function') {
      const result = this._value.reduce((a, b) => a && pred(b), true)
      return result && typeof result === 'boolean' ? result : false;
    } else {
      return false;
    }
  }

  toggleClass (className) {
    return this.forEach(e => e.classList.toggle(className))
  }

  hasClass (className) {
    return this.forAll(e => e.classList.contains(className))
  }

  hide () {
    return this.forEach(e => e.style.display = 'none')
  }

  show (displayStyle = 'initial') {
    return this.forEach(e => e.style.display = displayStyle)
  }

  isVisible () {
    return this.forAll(e => e.style.display !== 'none')
  }

  text () {
    let result = ''
    this.forEach(e => {
      if (e.textContent) {
        result += ` ${e.textContent}`
      }
    })

    return result.trim();
  }

  rm (selectorOrNode) {
    return this.forEach(e => {
      if (selectorOrNode instanceof String) {
        this.qs(selectorOrNode).forEach(e => e.remove())
      } else if (selectorOrNode instanceof DOMUtils) {
        selectorOrNode.element.remove()
      } else if (selectorOrNode instanceof HTMLElement) {
        selectorOrNode.remove()
      }
    })
  }

  add (item) {
    return this.forEach(e => {
      if (item instanceof String) {
        e.appendChild(vn(item).render())
      } else if (item instanceof DOMUtils) {
        e.appendChild(item.element)
      } else if (item instanceof HTMLElement) {
        e.appendChild(item)
      } else if (item instanceof VNode) {
        e.appendChild(item.render())
      }
    })
  }

  rmChildren () {
    return this.forEach(e => {
      while (e.firstChild) {
        e.removeChild(e.firstChild);
      }
    })
  }

  on(evt, handler){
    return this.forEach(e => e.addEventListener(evt, handler, false))
  }

}

const dom = (selector = '', el = document) => new DOMUtils(el).qs(selector)
export default dom 