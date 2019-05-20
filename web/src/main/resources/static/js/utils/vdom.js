import _ from './utils.js'

const classIdAttrSplit = /([\.#]?[a-zA-Z0-9\u007F-\uFFFF_:-]+|\[[^\[|\]]+\])/;
const notClassIdAttr = /^\.|#|\[/;

export class VNode {
  constructor (selectorStr, type = 'node') {
    this.type = type
    if (type === 'empty')
      return;
    if (type === 'text') {
      this.value = selectorStr
      return;
    }
    if (!selectorStr || !classIdAttrSplit.test(selectorStr)) {
      this.tag = 'DIV'
      return;
    }

    const parts = selectorStr.split(classIdAttrSplit)

    if (notClassIdAttr.test(parts[1]) || _.isDigit(parts[1].charCodeAt(0))) {
      this.tag = 'DIV'
    }

    for (let i = 0; i < parts.length; i++) {
      const part = parts[i]
      if (!part) continue;
      if (!this.tag) {
        this.tag = part.toUpperCase()
      } else if (part.charAt(0) === '.') {
        if (!this.classes) this.classes = []
        this.classes.push(part.substring(1, part.length))
      } else if (part.charAt(0) === '#' && !this.id) {
        this.id = part.substring(1, part.length)
      } else if (part.charAt(0) === '[') {
        if (!this.attributes) this.attributes = []
        this.attributes = this.attributes.concat(parseAttr(part))
      }
    }
  }

  wrap (node) {
    const parent = VNode.from(node)
    parent.children = [this]
    return parent;
  }

  append (node, ...tail) {
    if (!this.children) this.children = []
    this.children.push(VNode.from(node))
    if (tail)
      tail.map(VNode.from)
        .forEach(n => this.children.push(n))
    return this;
  }

  q (tag) {
    // console.log(tag, this.tag, tag.toUpperCase() === this.tag)
    if (this.tag === tag.toUpperCase()) return this;
    else if (this.children && this.children.length > 0) {
      for (const child of this.children) {
        const res = child.q(tag)
        if (res.type === 'empty')
          continue;
        else
          return res;
      }
      return empty()
    }

    else return empty();
  }

  attr (attrs = {}) {
    if (!this.attributes) this.attributes = []
    if (attrs.constructor.name === 'Object') {
      Object.entries(attrs).forEach(a => this.attributes.push(a))
    } else if (attrs.constructor.name === 'Array') {
      this.attributes = this.attributes.concat(attrs)
    }
    return this;
  }

  on (event, handler, opt = false) {
    if (!this.listeners) { this.listeners = [] }
    this.listeners.push([event, handler, opt])
    return this;
  }

  render () {
    try {
      if (this.type === 'text')
        return document.createTextNode(this.value)
      const result = document.createElement(this.tag)
      if (this.id) result.id = this.id;
      if (this.classes) this.classes.forEach(c => result.classList.add(c))
      if (this.attributes) this.attributes.forEach(([k, v]) => result.setAttribute(k, v))
      if (this.listeners) this.listeners.forEach(
        ([ev, fn, opt]) => result.addEventListener(ev, fn, opt)
      )
      if (this.children) {
        this.children
          .map(c => c.render())
          .forEach(ch => result.appendChild(ch))
      }

      return result;
    } catch (e) {
      console.log(this, e)

    }
  }

  static from (something) {
    if (!something) return text('')
    switch (something.constructor.name) {
      case 'VNode': return something
      case 'String': return new VNode(something)
      // case 'Array': return new VNode(something[0])
      // .append(text(something[1].toString()))
      default: return text('')
    }
  }
}

const parseAttr = attrStr =>
  attrStr
    .substring(1, attrStr.length - 1)
    .split(',')
    .map(kv => kv.trim().split("="))

export const empty = () => new VNode('', 'empty')
export const text = t => new VNode(t.toString(), 'text')

export const h1 = (...children) => vn('h1', ...children)
export const h2 = (...children) => vn('h2', ...children)
export const h3 = (...children) => vn('h3', ...children)
export const h4 = (...children) => vn('h4', ...children)
export const h5 = (...children) => vn('h5', ...children)
export const h6 = (...children) => vn('h6', ...children)

export const span = (...children) => vn('span', ...children)

export function vn () {
  const _args = Array.from(arguments)

  const [selector, props, children] = [
    _args.shift(),
    _.head(_args) && _.is(_.head(_args), Object) ? _args.shift() : {},
    _args || []
  ]

  // console.log(children, props, selector)
  const result = VNode.from(selector).attr(props)

  children.forEach(child => {
    if (typeof child === 'string' || typeof child === 'number') {
      result.append(text(child))
    } else if (child instanceof Array) {
      // console.log(child)
      if (child.length > 0) result.append.apply(result, child)
    } else {
      result.append(child)
    }
  })

  return result;

}