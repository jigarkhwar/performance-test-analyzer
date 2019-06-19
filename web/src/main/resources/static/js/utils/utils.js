const type = obj => obj && obj.constructor ? obj.constructor : obj
const is = (obj, objType) => type(obj) === objType
const isNot = (obj, objType) => type(obj) !== objType

const isEmpty = (a) => a && a.length && a.length > 0 ? false : true
const nonEmpty = (a) => !isEmpty(a)

const flatten = (arr) => {
  if(!Array.isArray(arr)) return []
  if(Array.prototype.flat) {
    return arr.flat()
  } else {
    const stack = [...arr]
    const res = []
    while (stack.length){
      const next = stack.pop()
      if(Array.isArray(next)) {
        stack.push(...next)
      } else {
        res.push(next)
      }
    }
    return res.reverse()
  }
}

export default {
  isDigit: c => c > 47 && c < 58,
  clearSlashes: path => path.toString().replace(/\/$/, '').replace(/^\//, ''),
  head: a => is(a, Array) && a[0] ? a[0] : undefined,
  type,
  is,
  isNot,
  isEmpty,
  nonEmpty,
  formatDate: (date, sep = '-') => {
    return is(date, Date) ?
      [date.getDate(), date.getMonth() + 1, date.getFullYear()]
        .reduce((res, part, i) =>
          i == 0 ? part.toString().padStart(2, '0') : `${res}${sep}${part.toString().padStart(2, '0')}`, ''
        )
      : ''
  },
  chunks: (a, n) => {
    if (isNot(a, Array) || n < 1) { return [] }
    const result = []
    const chunksCount = Math.ceil(a.length / n)
    for (let i = 0; i < chunksCount; i++) {
      let temp = []
      for (let j = 0; j < n; j++) {
        if (!a[j + i * n]) break
        temp.push(a[j + i * n])
      }
      result.push(temp)
    }
    return result
  },
  flatten
}