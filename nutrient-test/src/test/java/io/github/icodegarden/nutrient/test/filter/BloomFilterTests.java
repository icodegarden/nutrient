package io.github.icodegarden.nutrient.test.filter;

import io.github.icodegarden.nutrient.lang.filter.AbstractBloomFilter;
import io.github.icodegarden.nutrient.lang.filter.BloomFilter;
import io.github.icodegarden.nutrient.test.filter.AbstractBloomFilterTests;

/**
 * 
 * @author Fangfang.Xu
 *
 */
//@RunWith(MockitoJUnitRunner.class)
public class BloomFilterTests extends AbstractBloomFilterTests {

	@Override
	protected AbstractBloomFilter newBloomFilter(int count) {
		return new BloomFilter(count);
	}

}
