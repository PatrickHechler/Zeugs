package de.hechler.patrick.zeugs.objects;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * an implementation of the {@link Map} which can iterate fast, but has a slow search.
 * 
 * @author Patrick
 * @param <K>
 *            the 'keys'
 * @param <V>
 *            the 'values's
 */
public class PatrListMap <K, V> implements Map <K, V> {
	
	
	private int size;
	private PatrEntry <K, V>[] entries;
	
	public PatrListMap() {
		this(16);
	}
	
	@SuppressWarnings("unchecked")
	public PatrListMap(int len) {
		len = Math.max(len, 0);
		this.entries = new PatrEntry[len];
		for (int i = 0; i < len; i ++ ) {
			this.entries[i] = new PatrEntry <>(null, null);
		}
	}
	
	@Override
	public int size() {
		return size;
	}
	
	public PatrEntry <K, V> getEntry(int index) {
		if (index >= size) {
			throw new IndexOutOfBoundsException("index > size index=" + index + " size=" + size);
		}
		return entries[index];
	}
	
	public V getValue(int index) {
		if (index >= size) {
			throw new IndexOutOfBoundsException("index > size index=" + index + " size=" + size);
		}
		return entries[index].value;
	}
	
	public K getKey(int index) {
		if (index >= size) {
			throw new IndexOutOfBoundsException("index > size index=" + index + " size=" + size);
		}
		return entries[index].key;
	}
	
	public int getIndexOfKey(Object key) {
		return findIndex(key, true);
	}
	
	public int getFirstIndexOfValue(Object value) {
		return findIndex(value, false);
	}
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}
	
	@Override
	public boolean containsKey(Object key) {
		return -1 != findIndex(key, true);
	}
	
	@Override
	public boolean containsValue(Object value) {
		return -1 != findIndex(value, false);
	}
	
	@Override
	public V get(Object key) {
		int i = findIndex(key, true);
		if (i == -1) {
			return null;
		} else {
			return this.entries[i].value;
		}
	}
	
	public V getOrDefault(Object key, V defaultValue) {
		int i = findIndex(key, true);
		if (i == -1) {
			return defaultValue;
		} else {
			return this.entries[i].value;
		}
	}
	
	@Override
	public V put(K key, V value) {
		int i = findIndex(key, false);
		this.size ++ ;
		V val;
		if (i != -1) {
			val = this.entries[i].value;
		} else {
			increaseSize(1);
			i = this.size;
			val = null;
			this.entries[i].key = key;
		}
		this.entries[i].value = value;
		return val;
	}
	
	@Override
	public V remove(Object key) {
		int i = findIndex(key, true);
		if (i == -1) {
			return null;
		}
		V val = this.entries[i].value;
		remFromIndex(i);
		return val;
	}
	
	@Override
	public void putAll(Map <? extends K, ? extends V> m) {
		increaseSize(m.size());
		m.forEach(this::put);
	}
	
	@Override
	public void forEach(BiConsumer <? super K, ? super V> action) {
		for (int i = 0; i < size; i ++ ) {
			PatrEntry <K, V> e = entries[i];
			action.accept(e.key, e.value);
		}
	}
	
	public void replaceAll(java.util.function.BiFunction <? super K, ? super V, ? extends V> function) {
		for (int i = 0; i < size; i ++ ) {
			PatrEntry <K, V> e = entries[i];
			e.value = function.apply(e.key, e.value);
		}
	}
	
	public V putIfAbsent(K key, V value) {
		int i = findIndex(key, true);
		if (i != -1) {
			if (entries[i].value == null) {
				entries[i].value = value;
			}
			return entries[i].value;
		} else {
			put(key, value);
			return value;
		}
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		int i = findIndex(key, true);
		if (i == -1) {
			return false;
		}
		PatrEntry <K, V> e = entries[i];
		if (value == null) {
			if (e.value != null) {
				return false;
			}
		} else {
			if ( !value.equals(e.value)) {
				return false;
			}
		}
		remFromIndex(i);
		return true;
	}
	
	public boolean replace(K key, V oldValue, V newValue) {
		int i = findIndex(key, true);
		if (i == -1) {
			return false;
		}
		PatrEntry <K, V> e = entries[i];
		if (oldValue == null) {
			if (e.value != null) {
				return false;
			}
		} else {
			if (oldValue.equals(e.value)) {
				return false;
			}
		}
		e.value = newValue;
		return true;
	}
	
	public V replace(K key, V value) {
		int i = findIndex(key, true);
		if (i == -1) {
			return null;
		}
		PatrEntry <K, V> e = entries[i];
		V val = e.value;
		e.value = value;
		return val;
	}
	
	public V computeIfAbsent(K key, Function <? super K, ? extends V> mappingFunction) {
		int i = findIndex(key, true);
		if (i != -1) {
			if (entries[i].value != null) {
				return entries[i].value;
			}
		}
		V val = mappingFunction.apply(key);
		if (val == null) {
			return null;
		}
		if (i == -1) {
			increaseSize(1);
			i = size - 1;
		}
		entries[i].value = val;
		return val;
	}
	
	public V computeIfPresent(K key, java.util.function.BiFunction <? super K, ? super V, ? extends V> remappingFunction) {
		int i = findIndex(key, true);
		if (i == -1 || entries[i].value == null) {
			return null;
		}
		PatrEntry <K, V> e = entries[i];
		V val = remappingFunction.apply(e.key, e.value);
		if (val == null) {
			remFromIndex(i);
		} else {
			e.value = val;
		}
		return val;
	}
	
	@Override
	public V compute(K key, BiFunction <? super K, ? super V, ? extends V> remappingFunction) {
		int i = findIndex(key, true);
		V val;
		if (i == -1) {
			val = remappingFunction.apply(key, null);
			if (val == null) {
				return null;
			} else {
				increaseSize(1);
				entries[size - 1].value = val;
			}
		}
		val = remappingFunction.apply(key, entries[i].value);
		if (val == null) {
			remFromIndex(i);
		} else {
			entries[i].value = val;
		}
		return val;
	}
	
	@Override
	public V merge(K key, V value, BiFunction <? super V, ? super V, ? extends V> remappingFunction) {
		int i = findIndex(key, true);
		if (i == -1) {
			if (value == null) {
				return null;
			} else {
				increaseSize(1);
				entries[size - 1].value = value;
				return value;
			}
		}
		PatrEntry <K, V> e = entries[i];
		if (e.value == null) {
			e.value = value;
			return value;
		}
		value = remappingFunction.apply(e.value, value);
		if (value == null) {
			remFromIndex(i);
		} else {
			increaseSize(1);
			entries[size - 1].value = value;
		}
		return value;
	}
	
	@Override
	public Set <K> keySet() {
		return new PatrListSubSet <K>(e -> e.key, obj -> findIndex(obj, true), 1);
	}
	
	@Override
	public Collection <V> values() {
		return new PatrListSubSet <V>(e -> e.value, obj -> findIndex(obj, false), 2);
	}
	
	@Override
	public Set <Entry <K, V>> entrySet() {
		return new PatrListSubSet <Entry <K, V>>(e -> e, obj -> {
			if ( ! (obj instanceof Entry)) {
				return -1;
			}
			Entry <?, ?> e = (Entry <?, ?>) obj;
			int index = findIndex(e.getKey(), true);
			if (index == -1) {
				return -1;
			}
			Object val = e.getValue();
			if (val == null) {
				if (entries[index].value == null) {
					return index;
				}
			} else if (val.equals(entries[index].value)) {
				return index;
			}
			return -1;
		}, 3);
	}
	
	public static class PatrEntry <K, V> implements Entry <K, V> {
		
		private K key;
		private V value;
		
		public PatrEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		@Override
		public K getKey() {
			return key;
		}
		
		@Override
		public V getValue() {
			return value;
		}
		
		@Override
		public V setValue(V value) {
			V val = this.value;
			this.value = value;
			return val;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ( (key == null) ? 0 : key.hashCode());
			result = prime * result + ( (value == null) ? 0 : value.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			PatrEntry <?, ?> other = (PatrEntry <?, ?>) obj;
			if (key == null) {
				if (other.key != null) return false;
			} else if ( !key.equals(other.key)) return false;
			if (value == null) {
				if (other.value != null) return false;
			} else if ( !value.equals(other.value)) return false;
			return true;
		}
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append('[');
			builder.append(key);
			builder.append('=');
			builder.append(value);
			builder.append(']');
			return builder.toString();
		}
		
	}
	
	@FunctionalInterface
	public interface Extractor <E, K, V> {
		
		E extract(PatrEntry <K, V> entry);
		
	}
	
	@FunctionalInterface
	public interface IndexFinder {
		
		int findIndex(Object obj);
		
	}
	
	public class PatrListSubSet <E> implements Set <E> {
		
		private final Extractor <E, K, V> extractor;
		private final IndexFinder indexFinder;
		private final int typeid;
		
		public PatrListSubSet(Extractor <E, K, V> extractor, IndexFinder indexFinder, int typeid) {
			this.extractor = extractor;
			this.indexFinder = indexFinder;
			this.typeid = typeid;
		}
		
		@Override
		public int size() {
			return size;
		}
		
		@Override
		public boolean isEmpty() {
			return size == 0;
		}
		
		@Override
		public boolean contains(Object o) {
			return indexFinder.findIndex(o) != -1;
		}
		
		@Override
		public Iterator <E> iterator() {
			return new Iterator <E>() {
				
				int i;
				
				@Override
				public boolean hasNext() {
					return i < size;
				}
				
				@Override
				public E next() throws NoSuchElementException {
					if (i >= size) {
						throw new NoSuchElementException("fully iterated");
					}
					return extractor.extract(entries[i]);
				}
				
				@Override
				public void remove() {
					remFromIndex(i -- );
				}
				
			};
		}
		
		@Override
		public Object[] toArray() {
			return toArray(new Object[size]);
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] toArray(T[] a) {
			if (a.length > size) {
				a[size] = null;
			} else if (a.length < size) {
				a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
			}
			for (int i = 0, len = size; i < len; i ++ ) {
				a[i] = (T) extractor.extract(entries[i]);
			}
			return a;
		}
		
		@Override
		public boolean add(E e) {
			throw new UnsupportedOperationException("add of an inner set");
		}
		
		@Override
		public boolean remove(Object o) {
			int i = indexFinder.findIndex(o);
			if (i == -1) {
				return false;
			}
			remFromIndex(i);
			return true;
		}
		
		@Override
		public boolean containsAll(Collection <?> c) {
			for (Object obj : c) {
				if ( -1 == indexFinder.findIndex(obj)) {
					return false;
				}
			}
			return true;
		}
		
		@Override
		public boolean addAll(Collection <? extends E> c) {
			throw new UnsupportedOperationException("addAll of an inner set");
		}
		
		@Override
		public boolean retainAll(Collection <?> c) {
			int os = size;
			for (int i = 0; i < size; i ++ ) {
				E extract = extractor.extract(entries[i]);
				if (c.contains(extract)) {
					continue;
				}
				remFromIndex(i);
				i -- ;
			}
			return os != size;
		}
		
		@Override
		public boolean removeAll(Collection <?> c) {
			int os = size;
			for (Object obj : c) {
				int i = indexFinder.findIndex(obj);
				if (i != -1) {
					remFromIndex(i);
				}
			}
			return os != size;
		}
		
		@Override
		public void clear() {
			PatrListMap.this.clear();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ( (extractor == null) ? 0 : extractor.hashCode());
			result = prime * result + ( (indexFinder == null) ? 0 : indexFinder.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			@SuppressWarnings("unchecked")
			PatrListSubSet <?> other = (PatrListSubSet <?>) obj;
			if (this.typeid != other.typeid) return false;
			if ( !PatrListMap.this.equals(other.getEnclosingInstance())) return false;
			return true;
		}
		
		private final PatrListMap <K, V> getEnclosingInstance() {
			return PatrListMap.this;
		}
		
	}
	
	@Override
	public void clear() {
		while (size > 0) {
			entries[ -- size] = new PatrEntry <>(null, null);
		}
	}
	
	private void remFromIndex(int i) {
		System.arraycopy(entries, i + 1, entries, i, -- size - i);
		entries[size] = new PatrEntry <>(null, null);
	}
	
	private void increaseSize(int add) {
		if (this.entries.length < (this.size += add)) {
			grow(add);
		}
	}
	
	private void grow(int minGrow) {
		final int oldLen = this.entries.length;
		final int newLen = Math.max(oldLen | (oldLen >> 1), oldLen + minGrow);
		this.entries = Arrays.copyOf(this.entries, newLen);
		for (int i = oldLen; i < newLen; i ++ ) {
			this.entries[i] = new PatrEntry <>(null, null);
		}
	}
	
	private int findIndex(Object obj, boolean isKey) {
		for (int i = 0, len = this.size; i < len; i ++ ) {
			PatrEntry <K, V> entry = entries[i];
			if (obj == null) {
				if ( (isKey ? entry.key : entry.value) == null) {
					return i;
				}
			} else {
				if (obj.equals(isKey ? entry.key : entry.value)) {
					return i;
				}
			}
		}
		return -1;
	}
	
}
